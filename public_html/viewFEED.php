<?
      header('content-type: text/html; charset=utf-8');
    // 데이터베이스 접속 문자열. (db위치, 유저 이름, 비밀번호)
      $connect=@mysql_connect( "localhost", "root", "gncn") or
          die( "SQL server에 연결할 수 없습니다.");

     mysql_query("SET NAMES UTF8");
   // 데이터베이스 선택
     mysql_select_db("sotonge",$connect);

     $fid = $_POST['u_id'];
     $who = $_POST['u_pw'];

     //$fid = '2';
    // $result = mysql_query("SELECT SUBID,SUBNAME FROM sub_list ");
     $result = mysql_query("SELECT * FROM feed_cont WHERE FEEDID ='$fid' ");

     //result에 query 문으로 성공 실패 반환(TRUE,FALSE)

     if($result)         //쿼리가 성공적일때
     {
        $fields = mysql_fetch_array($result);      //필드의 갯수를 얻는다(열)

        echo $fields['CONTENTS']."\n";
        echo $fields['SCORE1']."\n";
        echo $fields['SCORE2']."\n";
        echo $fields['SCORE3']."\n";
        echo $fields['SCORE4']."\n";
        echo $fields['SCORE5']."\n";
        $result = mysql_query("SELECT * FROM feed_list WHERE FEEDID ='$fid' ");

        $res2 = mysql_fetch_array($result);
        $feedstate = $res2['FEEDSTATE'];


        if($feedstate == '1'&& $who =='1')
        {
            $sql = mysql_query("UPDATE feed_list SET FEEDSTATE= '2' WHERE FEEDID = '$fid'");
        }

        echo $feedstate."\n";
        if($feedstate == '3')
        {
            echo $fields['FEEDBACK']."\n";
        }
        else {
            echo "\n";
        }

      }
     else         //실패시(FALSE)\
     {
          die("mysql query error");
     }

?>
