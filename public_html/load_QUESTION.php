<?
      header('content-type: text/html; charset=utf-8');
    // 데이터베이스 접속 문자열. (db위치, 유저 이름, 비밀번호)
      $connect=@mysql_connect( "localhost", "root", "gncn") or
          die( "SQL server에 연결할 수 없습니다.");

     mysql_query("SET NAMES UTF8");
   // 데이터베이스 선택
     mysql_select_db("sotonge",$connect);

     $subn = $_POST['u_id'];

     $subidA=mysql_fetch_array(mysql_query("SELECT SUBID FROM sub_list WHERE SUBNAME='$subn'"));
     $subid=$subidA['SUBID'];

     //$fid = '2';
    // $result = mysql_query("SELECT SUBID,SUBNAME FROM sub_list ");
     $result = mysql_query("SELECT * FROM sub_opt WHERE SUBID ='$subid' ");

     //result에 query 문으로 성공 실패 반환(TRUE,FALSE)

     if($result)         //쿼리가 성공적일때
     {
        $fields = mysql_fetch_array($result);      //필드의 갯수를 얻는다(열)

        echo $fields['ADATE']."\n";
        echo $fields['QUESTION1']."\n";
        echo $fields['QUESTION2']."\n";
        echo $fields['QUESTION3']."\n";
        echo $fields['QUESTION4']."\n";
        echo $fields['QUESTION5']."\n";
    //    $result = mysql_query("SELECT * FROM feed_list WHERE FEEDID ='$fid' ");

  //      $res2 = mysql_fetch_array($result);
  //      $feedstate = $res2['FEEDSTATE'];


      }
     else         //실패시(FALSE)\
     {
          die("mysql query error");
     }

?>
