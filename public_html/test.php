<?php

function substring_index($subject, $delim, $count){
    if($count < 0){
        return implode($delim, array_slice(explode($delim, $subject), $count));
    }else{
        return implode($delim, array_slice(explode($delim, $subject), 0, $count));
    }
}
    $s = @mysql_connect("localhost", "root", "gncn") or die ("실패입니다.");

    $test = 'one.two.three.four.five';
    for($tc = substr_count($test, ".");$tc>=0;$tc--)
    {
        $tt[$tc] =  substring_index($test,'.',1);
        echo $tt[$tc];
        $test = substring_index($test,'.',-$tc);
      //echo $test;
    }
    mysql_close($s);



 ?>
