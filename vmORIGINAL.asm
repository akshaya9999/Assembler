// push constant 111
@111
D=A

// push constant 333
@333
D=A

// push constant 888
@888
D=A

// pop static 8
//SP--
@SP
M=M-1
A=M
D=M
@16
A=M
M=D
// pop static 3
//SP--
@SP
M=M-1
A=M
D=M
@17
A=M
M=D
// pop static 1
//SP--
@SP
M=M-1
A=M
D=M
@18
A=M
M=D
// push static 3
@17
D=M
@SP
A=M
M=D
//SP++
@SP
M=M+1
// push static 1
@18
D=M
@SP
A=M
M=D
//SP++
@SP
M=M+1
// push static 8
@16
D=M
@SP
A=M
M=D
//SP++
@SP
M=M+1
