# Operator-Precedence-Parser
The advantage of this method, is its simplicity of implementation, because in this method we consider all the terminals as operators. <br />we have three precedences:<br />
if a<.b: the precedence of the terminal a is less than the precedence of terminal b <br />
if a=b: the precedence of both terminals are equal. <br />
if a.>b: the precedence of the terminal a is more than the precedence of terminal b <br />
<br />
In this project, you can enter an input as below to see the result of the parse:<br />
(n+n)*n+n+n$ <br />
n+*n$ <br />
(n*n)+nn$ <br />
Any other expression containing n with the operators + and * with a $ sign at the end.<br />
The project will generate a parse table for your input with the help of firstterm and lastterm functions.<br />
The project will generate the parsing result in a three column table; and tells you if the parse failed or was successful.<br />


