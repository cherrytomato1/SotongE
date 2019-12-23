<?
      header('content-type: text/html; charset=utf-8');
    // 데이터베이스 접속 문자열. (db위치, 유저 이름, 비밀번호)
      $connect=@mysql_connect( "localhost", "root", "gncn") or
          die( "SQL server에 연결할 수 없습니다.");

     mysql_query("SET NAMES UTF8");
   // 데이터베이스 선택
     mysql_select_db("sotonge",$connect);

   // 세션 시작
  //   session_start();

     $id = $_POST['u_id'];
     $subn = $_POST['param0'];



    // $result = mysql_query("SELECT SUBID FROM sub_user WHERE USERID = '$id' ");
     $subid=mysql_fetch_array(mysql_query("SELECT SUBID FROM sub_list WHERE SUBNAME='$subn'"));

     //result에 query 문으로 성공 실패 반환(TRUE,FALSE)

//$subid=mysql_fetch_array(mysql_query("SELECT SUBID FROM sub_list WHERE SUBNAME='$subn'"));
     $sql = "INSERT INTO sub_user(SUBID, USERID) VALUES('$subid[SUBID]', '$id')";
     //sql에 insert 실행 결과 반환
     $result = mysql_query($sql);
     //result에 query 문으로 성공 실패 반환(TRUE,FALSE)

     if(!$result)   //실패시(FALSE)
          die("mysql query error");
     else
          echo "insert success"

?>
