
% Weights of all the edges/paths from X to Y containing W weight.

weights('G1','G5',4).
weights('G2','G5',6).
weights('G3','G5',8).
weights('G4','G5',9).
weights('G1','G6',10).
weights('G2','G6',9).
weights('G3','G6',3).
weights('G4','G6',5).
weights('G5','G7',3).
weights('G5','G10',4).
weights('G5','G11',6).
weights('G5','G12',7).
weights('G5','G6',7).
weights('G5','G8',9).
weights('G6','G8',2).
weights('G6','G12',3).
weights('G6','G11',5).
weights('G6','G10',9).
weights('G6','G7',10).
weights('G7','G10',2).
weights('G7','G11',5).
weights('G7','G12',7).
weights('G7','G8',10).
weights('G8','G9',3).
weights('G8','G12',3).
weights('G8','G11',4).
weights('G8','G10',8).
weights('G10','G15',5).
weights('G10','G11',2).
weights('G10','G12',5).
weights('G11','G15',4).
weights('G11','G13',5).
weights('G11','G12',4).
weights('G12','G13',7).
weights('G12','G14',8).
weights('G15','G13',3).
weights('G13','G14',4).
weights('G14','G17',5).
weights('G14','G18',4).
weights('G17','G18',8).

start('G1').
start('G2').
start('G3').
start('G4').

end('G17').

% Returns whether there is an edge between X and Y.
edge(X,Y):- weights(X,Y,_).

undirected_edge(X,Y):- weights(X,Y,_).
undirected_edge(X,Y):- weights(Y,X,_).

% Global Variables for storing the best weight and best paths.
:-dynamic(best_weight/1).
:-dynamic(best_paths/1).
best_weight(999999).


% Function for checking if a given list is empty or not.
is_empty([]).

% check_path function sees in the given list if the edge is valid then recursibely move to the next edge otherwise break. If the last element is the ending vertex then only return true.
check_path(List):-
	[H|T] = List,
	is_empty(T),
	end(H).

check_path(List):-
	[X1,X2|_] = List,
	undirected_edge(X1,X2),
	[_|T] = List,
	check_path(T).

% valid Function is the one that is called by the user. It will check first if the start of the list is one of the given starting nodes and then call check_path. If the given input is not a valid path then it will go to the second function of valid.
valid(List):-
	[H|_] = List,
	start(H),
	check_path(List),
	write("Path Found").

valid(_):-
	write("Path Not Found").

% print_path is used to print a given list containing vertices with arrows in middle of them.
print_path(_,[]).

print_path(Prefix,[H|T]):-
	is_empty(T),
	write(Prefix),
	write(H),
	nl.	

print_path(Prefix,[H|T]):-
	\+ is_empty(T),
	format("~w~w -> ",[Prefix,H]),
	print_path("",T).

% Given Y as the maximum Sum observed till now and Sum is the current Path's sum.
% 	If Y > Sum update Y to sum and delete all previous paths store the current path.
%	If Y == Sum add current Path to best_paths.

update(Y,Sum,Path):-
	Y > Sum,
	retract(best_weight(Y)),
	asserta(best_weight(Sum)),
	retractall(best_paths(_)),
	assertz(best_paths(Path)).

update(Y,Sum,Path):-
	Y =:= Sum,
	assertz(best_paths(Path)).


% dfs_util is the utility function called by dfs function.
% 	If current Path's sum is greater than best path sum, we can drop it and dont need to go ahead in this path's direction.
% 	If we have reached the end then call the update function.
% 	Else look at all edges from current node and call dfs on them.

dfs_util(_,Sum,_):-
	best_weight(Y),
	Sum >= Y.

dfs_util(C,Sum,Path):-
	end(C),
	append(Path,[C],NewPath),	
	best_weight(Y),
	update(Y,Sum,NewPath).

dfs_util(C,Sum,Path):-
	append(Path,[C],NewPath),
	edge(C, R),
	weights(C,R,Y),
	NewSum is Sum+Y,	
	dfs_util(R,NewSum,NewPath),
	fail.

% start dfs function from all possible starting locations. 
dfs:-
	start(C),	
	dfs_util(C,0,[]),
	fail.

% print_paths prints all of the best paths using best_paths function.
print_paths:-
	best_paths(Y),
	print_path("Path: ",Y),
	fail.

% print_weight checks if there is any path. If there is it prints the result.  
print_weight(Z):-
	Z =:= 999999,
	print("No paths found from given start to end").

print_weight(Z):-
	format("Weight of Path: ~w ~n",[Z]).

% precheck_database alerts the user if there is any negative distance.
precheck_database:-
	weights(_,_,X) , X < 0,
	print("***Negative Weights Found***"),
	nl.

% optimal function is called by the user which will first check the database, then do dfs, then print the results.
optimal:-
	\+ precheck_database,
	\+ dfs,
	write("The following paths are the shortest:"),
	nl,
	\+ print_paths,
	best_weight(Z),
	print_weight(Z).

