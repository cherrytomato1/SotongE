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

    // $pid = $_POST['u_id'];
     $subn = $_POST['u_pw'];
  //   $subn = '모바일 프로그래밍';

     $result=mysql_fetch_array(mysql_query("SELECT SUBID FROM sub_list WHERE SUBNAME='$subn'"));
     $subid = $result['SUBID'];
     //$subid = '1001';
     //sql에 insert 실행 결과 반환
     $result = mysql_query("SELECT * FROM board_list WHERE SUBID = '$subid' ORDER BY BOARDDATE DESC");

     //result에 query 문으로 성공 실패 반환(TRUE,FALSE)

     if($result)         //쿼리가 성공적일때
     {
      //  $fields = mysql_num_fields($result);      //필드의 갯수를 얻는다(열)

        while($row = mysql_fetch_array($result))    //$row에 result를 넣고 넣지못하면 false로 반복끝
        {
            $bid = $row['BOARDID'];
            $bdate= $row['BOARDDATE'];
            $btext =$row['CONTENTS'];
            $btitle= $row['TITLE'];
    //              echo $subid."\n";                   //row의 i번쨰 값 출력

            //$name=mysql_fetch_array(mysql_query("SELECT SUBNAME FROM sub_list WHERE SUBID='$subid'"));
            //subid에 따른 id
            //echo $row['SUBNAME']."\n";
            echo $bid."\n";
            echo $btitle."\n";
            echo $btext."\n";
            echo $bdate."\n";
        }
      }
     else         //실패시(FALSE)\
     {
          die("mysql query error");
     }

?>
