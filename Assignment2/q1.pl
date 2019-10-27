%The mapping of each number to corresponding alphabet
mapping('A','1').
mapping('B','2').
mapping('C','3').
mapping('D','4').
mapping('E','5').
mapping('F','6').
mapping('G','7').
mapping('H','8').
mapping('I','9').
mapping('J','10').
mapping('K','11').
mapping('L','12').
mapping('M','13').
mapping('N','14').
mapping('O','15').
mapping('P','16').
mapping('Q','17').
mapping('R','18').
mapping('S','19').
mapping('T','20').
mapping('U','21').
mapping('V','22').
mapping('W','23').
mapping('X','24').
mapping('Y','25').
mapping('Z','26').

% Global variable for maintaining the count
:-dynamic(count/1).

% If the list is empty then the solution is feasible.
decode_util([],Curr):-
	count(Y),
	Y1 is Y+1,
	retract(count(Y)),
	atomic_list_concat(Curr,'',String),
	format("~w ~n",[String]),
	asserta(count(Y1)),	
	fail.

% Taking one character.
decode_util([X|T],Curr):-
	mapping(Y,X),
	append(Curr,[Y],NewCurr),
	decode_util(T,NewCurr).

% Taking two character.
decode_util([X1,X2|T],Curr):-
	atom_concat(X1,X2,X),
	mapping(Y,X),
	append(Curr,[Y],NewCurr),
	decode_util(T,NewCurr).

% Main function to handle the empty string.
decode(""):-
	format("These are the possible encodings:-~n"),
	format("Total possible encodings:- 0 ~n"),
	!.

% Main function to handle the empty string.
decode(''):-
	format("These are the possible encodings:-~n"),
	format("Total possible encodings:- 0 ~n"),
	!.

% Main function removes the set count if already set, initalize it to 0, then process.	
decode(X):-
	(atom(X);string(X)),
	retractall(count(_)),
	asserta(count(0)),
	atom_chars(X,List),
	format("These are the possible encodings:-~n"),
	\+ decode_util(List,[]),
	count(Y),
	format("Total possible encodings:- ~w ~n",[Y]),
	!.	

% Handles error if the input is not a string.
decode(X):-
	\+ (atom(X);string(X)),
	format("No possible encodings as input is not a string.~n"),
	false.

