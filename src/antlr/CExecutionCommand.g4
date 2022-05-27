grammar CExecutionCommand;


ID  :   [a-zA-z_] [a-zA-z]*;
AS  :   '-' (ID | STRING);
NEXT:   '>';
prog:   (command | comment)*;
command: execution (SPACE* NEXT SPACE* execution)*;
execution:	(ID SPACE (AS SPACE rvalue)*);
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

