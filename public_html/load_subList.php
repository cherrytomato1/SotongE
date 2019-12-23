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


     //sql에 insert 실행 결과 반환
    // $result = mysql_query("SELECT SUBID,SUBNAME FROM sub_list ");
     $result = mysql_query("SELECT SUBNAME FROM sub_list ");

     //result에 query 문으로 성공 실패 반환(TRUE,FALSE)

     if($result)         //쿼리가 성공적일때
     {
        $fields = mysql_num_fields($result);      //필드의 갯수를 얻는다(열)

        while($row = mysql_fetch_array($result))    //$row에 result를 넣고 넣지못하면 false로 반복끝
        {
            for($i=0;$i<$fields;$i++)             //row에서 열의 갯수만큼 출력
            echo $row[$i]."\n";                   //row의 i번쨰 값 출력
        }
      }
     else         //실패시(FALSE)\
     {
          die("mysql query error");
     }

?>
