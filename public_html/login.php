<?
      header('content-type: text/html; charset=utf-8');
    // 데이터베이스 접속 문자열. (db위치, 유저 이름, 비밀번호)
      $connect=@mysql_connect( "localhost", "root", "gncn") or
          die( "SQL server에 연결할 수 없습니다.");

     mysql_query("SET NAMES UTF8");
   // 데이터베이스 선택
     mysql_select_db("sotonge",$connect);

     $id = $_POST['u_id'];
     $pw = $_POST['u_pw'];

     $sql = "SELECT * FROM user WHERE USERID = '$id'";
     //sql에 insert 실행 결과 반환
     $result = mysql_query($sql);

     if($result)
     {
          $row = mysql_fetch_array($result);

          if(($pw==$row['PASSWORD'])&&!is_null($pw))
          {
            //  $name = mysql_fetch_array($nres=mysql_query($nsql="SELECT NAME FROM user WHERE USERID = '$id'"));
              echo $row['USERID'];
              echo "\n";
              echo $row['NAME'];
              echo "\n";
              echo $row['ENABLE'];
          }
          else
          {
              $result =mysql_query("SELECT * FROM professor WHERE PID = '$id'");
              if($result)
              {
                  $row = mysql_fetch_array($result);
                  if(($pw==$row['PPWD'])&&(!is_null($pw)))
                  {
                      echo $row['PID'];
                      echo "\n";
                      echo $row['PNAME'];
                      echo "\n";
                      echo '4';
                  }
                  else{
                      echo "ERROR!";
                  }
              }
              else{
                  echo "ERROR!";
              }
          }
      }
     else         //실패시(FALSE)\
     {
          die("mysql query error");
     }

?>
