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

     $uid = $_POST['u_id'];
     $subn = $_POST['u_pw'];

  //   $subn = '데이터 통신';
  //   $uid = '20150982';



     $subidA=mysql_fetch_array(mysql_query("SELECT SUBID FROM sub_list WHERE SUBNAME='$subn'"));
     $subid=$subidA['SUBID'];

    // $subid ='1007';

     //sql에 insert 실행 결과 반환
     $result = mysql_query("SELECT * FROM sub_user WHERE SUBID='$subid' and USERID='$uid' ");

     //echo $subid;

     //result에 query 문으로 성공 실패 반환(TRUE,FALSE)

     if($result)         //쿼리가 성공적일때
     {
      //  $fields = mysql_num_fields($result);      //필드의 갯수를 얻는다(열)
            $lfeedA = mysql_fetch_array($result);
            $lfeed= $lfeedA['LASTFEED'];
            //subid에 따른 id

            $termA = mysql_fetch_array(mysql_query("SELECT ADATE FROM sub_opt WHERE SUBID='$subid'"));
            $term = $termA['ADATE'];

            echo $lfeed."\n";
            echo $term."\n";


      }
     else         //실패시(FALSE)\
     {
          die("mysql query error");
     }

?>
