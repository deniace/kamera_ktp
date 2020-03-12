package com.deni.kameraktp.coba;

/**
 * Created by Deni Supriyatna on 12/03/2020.
 * Email : denisupriyatna01@gmail.com
 */
public class IniDiServerCi {
    //scrip buat di server ci menangkap hasil nya
//    public function uploadkyc()
//    {
//        $table = "tb_kyc";
//        $data = array();
//        $hp_number = $this->input->post("hp_number");
//        $idcard_type = $this->input->post("idcard_type");
//        $idcard_image = $this->input->post("idcard_image");
//        $mother_name = $this->input->post("mother_name");
//        $mark_1 = $this->input->post("mark_1");
//        $mark_2 = $this->input->post("mark_2");
//        $mark_3 = $this->input->post("mark_3");
//        $mark_4 = $this->input->post("mark_4");
//        $mark_5 = $this->input->post("mark_5");
//        $mark_6 = $this->input->post("mark_6");
//        $mark_7 = $this->input->post("mark_7");
//        $mark_8 = $this->input->post("mark_8");
//        $mark_9 = $this->input->post("mark_9");
//        $mark_10 = $this->input->post("mark_10");
//        $mark_11 = $this->input->post("mark_11");
//        $mark_12 = $this->input->post("mark_12");
//        $mark_13 = $this->input->post("mark_13");
//        $mark_14 = $this->input->post("mark_14");
//        $mark_15 = $this->input->post("mark_15");
//
//        $file_name = $hp_number . ".jpg";
//        $decodedImage = base64_decode($idcard_image);
//        $path = './uploads/kyc';
//        if (!file_exists($path)) {
//            mkdir($path);
//        }
//        file_put_contents($path . "/" . $file_name, $decodedImage);
//
//        $query = $this->db->get_where($table, array('hp_number' => $hp_number));
//
//        $dataArray = array(
//                'hp_number' => $hp_number,
//                'idcard_type' => $idcard_type,
//            'idcard_image' => $file_name,
//            'mother_name' => $mother_name,
//            'mark_1' => $mark_1,
//            'mark_2' => $mark_2,
//            'mark_3' => $mark_3,
//            'mark_4' => $mark_4,
//            'mark_5' => $mark_5,
//            'mark_6' => $mark_6,
//            'mark_7' => $mark_7,
//            'mark_8' => $mark_8,
//            'mark_9' => $mark_9,
//            'mark_10' => $mark_10,
//            'mark_11' => $mark_11,
//            'mark_12' => $mark_12,
//            'mark_13' => $mark_13,
//            'mark_14' => $mark_14,
//            'mark_15' => $mark_15
//    );
//
//        if ($query->num_rows >= 1) {
//            //update
//            //udah ada di db
//            $result = $query->row();
//            $this->db->where('hp_number', $result->hp_number);
//            $this->db->update($table, $dataArray);
//            $success = $this->db->affected_rows();
//            $message = "update sukses...";
//        } else {
//            //insert
//            // belum ada di db
//            $this->db->insert($table, $dataArray);
//            $success = $this->db->affected_rows();
//            $message = "save sukses...";
//        }
//
//        if ($success > 0) {
//            $data["responce"] = true;
//            $data["data"] = $message;
//        } else {
//            $data["responce"] = false;
//            $data["data"] = "Save failed...";
//        }
//
//        echo json_encode($data);
//    }
}
