grammar CExecutionShell;



AS  :   '-' (ID | STRING);
NEXT:   '>';
shell:   (command | comment)*;
command: execution (SPACE* NEXT SPACE* execution)*;
execution:	(ID (SPACE AS SPACE rvalue)*);

// 右值
rvalue: STRING | ID | INT;
comment: COMMENT_INPUT | LINE_COMMENT;


STRING: DQUOTA_STRING | SQUOTA_STRING | BQUOTA_STRING;
INT     : SIGN? [0-9]+ ;



fragment SIGN : '-' ;

fragment DQUOTA_STRING:
	'"' ('\\' . | '""' | ~('"' | '\\'))* '"';
fragment SQUOTA_STRING:
	'\'' ('\\' . | '\'\'' | ~('\'' | '\\'))* '\'';
fragment BQUOTA_STRING:
	'`' ('\\' . | '``' | ~('`' | '\\'))* '`';

COMMENT_INPUT: '/*' .*? '*/' ;
LINE_COMMENT: ( '// ' ~[\r\n]* ('\r'? '\n' | EOF)
    | '//' ('\r'? '\n' | EOF) );
SPACE: ' '+;
ID  :   [a-zA-Z_] [a-zA-Z_0-9]*;

