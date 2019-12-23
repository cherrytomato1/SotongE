<?
      header('content-type: text/html; charset=utf-8');
    // 데이터베이스 접속 문자열. (db위치, 유저 이름, 비밀번호)
      $connect=@mysql_connect( "localhost", "root", "gncn") or
          die( "SQL server에 연결할 수 없습니다.");

     mysql_query("SET NAMES UTF8");
   // 데이터베이스 선택
     mysql_select_db("sotonge",$connect);

     $lect = $_POST['u_id'];


     $result=mysql_fetch_array(mysql_query("SELECT PID FROM sub_list WHERE SUBNAME = '$lect'"));

     $pid = $result['PID'];

     $result = mysql_query("SELECT PNAME FROM professor WHERE PID = '$pid'");
     if($result)
     {
       $pname = mysql_fetch_array($result);
       echo $pname['PNAME'];
     }

     else         //실패시(FALSE)\
     {
          die("mysql query error");
     }

?>
