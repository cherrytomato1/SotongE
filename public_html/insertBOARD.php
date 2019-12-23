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

     $subn = $_POST['u_id'];
     $btitle = $_POST['u_pw'];
     $bcont = $_POST['param0'];
     $bdate = $_POST['param1'];
     $bid = $_POST['param2'];

     //$subn = '데이터 통신';

     $subidA=mysql_fetch_array(mysql_query("SELECT SUBID FROM sub_list WHERE SUBNAME='$subn'"));
     $subid=$subidA['SUBID'];

     echo $subid;

    // $result = mysql_query("SELECT SUBID FROM sub_user WHERE USERID = '$id' ");
     //$subid=mysql_fetch_array(mysql_query("SELECT SUBID FROM sub_list WHERE SUBNAME='$subn'"));

     //result에 query 문으로 성공 실패 반환(TRUE,FALSE)

     //$subid=mysql_fetch_array(mysql_query("SELECT SUBID FROM sub_list WHERE SUBNAME='$subn'"));
     if($bdate==1)
     {
       $result=mysql_query("UPDATE board_list SET TITLE='$btitle',CONTENTS='$bcont' WHERE BID = '$bid'");
     }
     else{
       $result=mysql_query("INSERT INTO board_list(SUBID,TITLE,CONTENTS,BOARDDATE) VALUES('$subid','$btitle','$bcont','$bdate')");
     }


     //sql에 insert 실행 결과 반환
     //result에 query 문으로 성공 실패 반환(TRUE,FALSE)

     if(!$result)   //실패시(FALSE)
          die("mysql query error");
     else
          echo "insert success"

?>
