package com.deni.kameraktp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Base64;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.deni.kameraktp.databinding.ActivityCameraVerifikasiKtpJavaBinding;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Created by Deni Supriyatna
 */


public class CameraVerifikasiKtpJavaActivity extends AppCompatActivity {

    //3
    // data binding variable
    private ActivityCameraVerifikasiKtpJavaBinding binding;
    //
    private static final int MAX_PREVIEW_WIDTH = 1920;
    private static final int MAX_PREVIEW_HEIGHT = 1080;
    //A CameraDevice represent one physical device's camera. In this attribute, we save the ID of the current CameraDevice
    private String mCameraID;
    //This is the view (TextureView) that we'll be using to "draw" the preview of the Camera
    private TextureView mTextureView;
    //The CameraCaptureSession for camera preview
    private CameraCaptureSession mCaptureSession;
    //A reference to the opened CameraDevice
    private CameraDevice mCameraDevice;
    //The Size of camera preview.
    private Size mPreviewSize;

    //An additional thread for running tasks that shouldn't block the UI
    private HandlerThread mBackgroundThread;
    //A Handler for running tasks in the background
    private Handler mBackgroundHandler;
    //An ImageReader that handles still image capture
    private ImageReader mImageReader;
    //CaptureRequest.Builder for the camera preview
    private CaptureRequest.Builder mPreviewRequestBuilder;
    //CaptureRequest generated by mPreviewRequestBuilder
    private CaptureRequest mPreviewRequest;
    //A Semaphore to prevent the app from exiting before closing the camera.
    private Semaphore mCameraOpenCloseLock = new Semaphore(1);

    //Constant ID of the permission request
    private static final int REQUEST_CAMERA_PERMISSION = 1;

    private Camera mCamera;
    private ImageButton ibCapture;

    //buat intent extra
    private final String ktp_bitmap = "KTPBITMAP";
    private final String ktp_base64 = "KTPBASE64";

    // apakah ada flash
    private boolean hasFlash = false;
    private boolean isFLashOn = false;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_camera_verifikasi_ktp_java);
//        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
//        getActionBar().hide();
        hasFlash = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        mTextureView = (TextureView) findViewById(R.id.tv_camera_ktp_java);
        ibCapture = findViewById(R.id.btn_capture_ktp_java);
        ibCapture.setOnClickListener(capture);
        binding.btnFlashCaptureKtpJava.setOnClickListener(onFlash);

    }

    @Override
    public void onResume() {
        super.onResume();
        startBackgroundThread();

        //when the screen is turned off and turned back on, the surfaceTexture is already
        //avaiable, and "onSurfaceTextureAvaiable" will not be called. in thad case we can open
        // a camera and start preview from here (otherwise, we wait until the surface is ready in the SurfaceTextureListener).

        if (mTextureView.isAvailable()) {
            openCamera(mTextureView.getWidth(), mTextureView.getHeight());
        } else {
            mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
        }
    }

    View.OnClickListener capture = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bitmap bitmap = cropBitmap(texture2Bitmap());
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bs);
            byte[] ba = bs.toByteArray();
            String encoded_64 = Base64.encodeToString(ba, Base64.DEFAULT);
//            Log.d("image bit array", "onClick: "+encoded_64);

            Intent intent = new Intent(CameraVerifikasiKtpJavaActivity.this, VerifikasiFotoKtpJavaActivity.class);
            intent.putExtra(ktp_bitmap, ba);
            intent.putExtra(ktp_base64, encoded_64);
            startActivity(intent);
        }
    };

    View.OnClickListener onFlash = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(hasFlash){
                if(isFLashOn){
                    binding.btnFlashCaptureKtpJava.setImageResource(R.drawable.ic_flash_off);
                    flashLightOff();
                    isFLashOn = false;
                }else {
                    binding.btnFlashCaptureKtpJava.setImageResource(R.drawable.ic_flash_on);
                    flashLightOn();
                    isFLashOn = true;
                }
            }else {
                Toast.makeText(CameraVerifikasiKtpJavaActivity.this, R.string.flash_not_available, Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void flashLightOn() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraManager.setTorchMode(mCameraID, true);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void flashLightOff() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraManager.setTorchMode(mCameraID, false);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    // crop gambar yang di dapat dari texture view menjadi ukuran yang sesuai
    private Bitmap cropBitmap(Bitmap bitmap) {
        int h = binding.ivCameraLayerKtpJava.getMeasuredHeight();
        int w = binding.ivCameraLayerKtpJava.getMeasuredWidth();
        int height = (bitmap.getHeight() - h) / 2;
        Bitmap cropBitmap = null;
        cropBitmap = Bitmap.createBitmap(bitmap, 0, height, w, h);
        return cropBitmap;
    }

    // menggambil data gambar dari texture view
    private Bitmap texture2Bitmap() {
        Bitmap bitmap = mTextureView.getBitmap();
        return bitmap;
    }

    private final TextureView.SurfaceTextureListener mSurfaceTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            openCamera(width, height);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            configureTransform(width, height);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    };

    @Override
    public void onPause() {
        closeCamera();
        stopBackgroundThread();
        super.onPause();
    }

    private final TextureView.SurfaceTextureListener surfaceTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            openCamera(width, height);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            configureTransform(width, height);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    };

    //CameraDevice.StateCallback is called when CameraDevice changes its state
    private final CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            // this methode is called when the camera is opened. We start camera preview Here
            mCameraOpenCloseLock.release();
            mCameraDevice = camera;
            createCameraPreviewSession();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            mCameraOpenCloseLock.release();
            camera.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            mCameraOpenCloseLock.release();
            camera.close();
            mCameraDevice = null;
            finish();
        }
    };

    // Those are methods that uses the Camera2 APIs
    private void openCamera(int width, int height) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission();
            return;
        }
        setUpCameraOutputs(width, height);
        configureTransform(width, height);

        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException(getString(R.string.time_out_waiting_camera));
            }
            manager.openCamera(mCameraID, mStateCallback, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(getString(R.string.interupted_camera_opening), e);
        }
    }

    //closes the current camera
    private void closeCamera() {
        try {
            mCameraOpenCloseLock.acquire();
            if (null != mCaptureSession) {
                mCaptureSession.close();
                mCaptureSession = null;
            }
            if (null != mCameraDevice) {
                mCameraDevice.close();
                mCameraDevice = null;
            }
            if (null != mImageReader) {
                mImageReader.close();
                mImageReader = null;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(getString(R.string.interupted_camera_clossing), e);
        } finally {
            mCameraOpenCloseLock.release();
        }
    }

    private void setUpCameraOutputs(int width, int height) {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            for (String cameraId : manager.getCameraIdList()) {
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);

                //we dont use a front facing camera
                Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT) {
                    continue;
                }

                StreamConfigurationMap map = characteristics.get(
                        CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP
                );
                if (map == null) {
                    continue;
                }

                //for still image captures, we use the largest available size
                Size largest = Collections.max(
                        Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)),
                        new CompareSizesByArea()
                );
                mImageReader = ImageReader.newInstance(largest.getWidth(), largest.getHeight(), ImageFormat.JPEG, /*maxImages*/ 2);
                mImageReader.setOnImageAvailableListener(null, mBackgroundHandler);

                Point displaySize = new Point();
                getWindowManager().getDefaultDisplay().getSize(displaySize);
                int rotatedPreviewWidth = width;
                int rotatedPreviewHeight = height;
                int maxPreviewWidth = displaySize.x;
                int maxPreviewHeight = displaySize.y;

                if (maxPreviewWidth > MAX_PREVIEW_WIDTH) {
                    maxPreviewWidth = MAX_PREVIEW_WIDTH;
                }

                if (maxPreviewHeight > MAX_PREVIEW_HEIGHT) {
                    maxPreviewHeight = MAX_PREVIEW_HEIGHT;
                }

                // Danger! Attempting to use too large a preview size could  exceed the camera
                // bus' bandwidth limitation, resulting in gorgeous previews but the storage of
                // garbage capture data.

                mPreviewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class),
                        rotatedPreviewWidth, rotatedPreviewHeight, maxPreviewWidth, maxPreviewHeight, largest);

                mCameraID = cameraId;
                return;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            // currently an NPE is thrown when the Camera2API is used but not Supported on tht
            // device this code run
            Toast.makeText(CameraVerifikasiKtpJavaActivity.this, R.string.device_not_support, Toast.LENGTH_SHORT).show();
        }
    }

    //Permissions related methods For Android API 23+
    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            new AlertDialog.Builder(CameraVerifikasiKtpJavaActivity.this)
                    .setMessage("MISI")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(CameraVerifikasiKtpJavaActivity.this,
                                    new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                        }
                    })
                    .setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                    .create();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permission, @NonNull int[] grandResult) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grandResult.length != 1 || grandResult[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, R.string.error_not_granted, Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permission, grandResult);
        }
    }

    //Creates a new CameraCaptureSession for camera preview
    private void createCameraPreviewSession() {
        try {
            SurfaceTexture texture = mTextureView.getSurfaceTexture();
            assert texture != null;
            // We configure the size of default buffer to be the size of camera preview we want.
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            // This is the output surface we need to start preview
            Surface surface = new Surface(texture);
            //we set up a CaptureRequest.Builder with the output Surface
            mPreviewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mPreviewRequestBuilder.addTarget(surface);

            //here we create a CameraCaptureSession for camera preview
            mCameraDevice.createCaptureSession(Arrays.asList(surface, mImageReader.getSurface()),
                    new CameraCaptureSession.StateCallback() {
                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                            // the camera is already close
                            if (null == mCameraDevice) {
                                return;
                            }
                            //when the session is ready, we start displaying the preview
                            mCaptureSession = cameraCaptureSession;
                            try {
                                // auto focus should be continous  for camera preview
                                mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                                        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                                //finally, we start displaying the camera preview
                                mPreviewRequest = mPreviewRequestBuilder.build();
                                mCaptureSession.setRepeatingRequest(mPreviewRequest, null, mBackgroundHandler);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                            showToast(getString(R.string.failed));
                        }
                    }, null
            );
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    // Background thread / handler methods
    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("CameraBackground");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    private void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Utility methods
    private static Size chooseOptimalSize(Size[] choises, int textureViewWidth, int textureViewHeight, int maxWidth, int maxHeight, Size aspectRatio) {

        //collect the supported resolutions that are at least as big as the preview Surface
        List<Size> bigEnough = new ArrayList<>();
        // collect the supported resolutions that are smaller than the preview surface
        List<Size> notBigEnough = new ArrayList<>();

        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choises) {
            if (option.getWidth() <= maxWidth && option.getHeight() <= maxHeight && option.getHeight() == option.getWidth() * h / w) {
                if (option.getWidth() >= textureViewWidth && option.getHeight() >= textureViewHeight) {
                    bigEnough.add(option);
                } else {
                    notBigEnough.add(option);
                }
            }
        }

        // Pick the smallest of those big enough. if there no one big enough, pick the largest of those not big enough
        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else if (notBigEnough.size() > 0) {
            return Collections.max(notBigEnough, new CompareSizesByArea());
        } else {
            Log.e("Camera2", "Couldn't find any suitable preview size");
            return choises[0];
        }
    }

    //This method congfigures the neccesary Matrix transformation to mTextureView
    private void configureTransform(int viewWidth, int viewHeight) {
        if (null == mTextureView || null == mPreviewSize) {
            return;
        }
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, mPreviewSize.getHeight(), mPreviewSize.getWidth());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerX();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max(
                    (float) viewHeight / mPreviewSize.getHeight(),
                    (float) viewWidth / mPreviewSize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        } else if (Surface.ROTATION_180 == rotation) {
            matrix.postRotate(180, centerX, centerY);
        }
        mTextureView.setTransform(matrix);
    }

    // This method compares two Sizes based on their areas.
    static class CompareSizesByArea implements Comparator<Size> {
        @Override
        public int compare(Size lhs, Size rhs) {
            //we cast here to ensure the multiplications wont overflow
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() - (long) rhs.getWidth() * rhs.getHeight());
        }
    }

    /**
     * Shows a {@link Toast} on the UI thread
     *
     * @param text the message
     */
    private void showToast(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(CameraVerifikasiKtpJavaActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
