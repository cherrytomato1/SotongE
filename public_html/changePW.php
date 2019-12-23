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
     $pw = $_POST['u_pw'];
     $npw = $_POST['param0'];

  //   $pw = "all1";

     $sql = "SELECT PASSWORD FROM user WHERE USERID = '$id'";

     //sql에 insert 실행 결과 반환
     $result = mysql_query($sql);

     //result에 query 문으로 성공 실패 반환(TRUE,FALSE)

     if($result)
     {
          $row = mysql_fetch_array($result);


          if(($pw==$row['PASSWORD'])&&!is_null($pw))
          {
            //  $name = mysql_fetch_array($nres=mysql_query($nsql="SELECT NAME FROM user WHERE USERID = '$id'"));
              mysql_query("UPDATE user SET PASSWORD='$npw' WHERE USERID = '$id'");
              echo "TRUE";
          }
          else
          {
              echo "ERROR!";
              //패스워드 틀림~!
          }
      }
     else         //실패시(FALSE)\
     {
          die("mysql query error");
     }

?>
