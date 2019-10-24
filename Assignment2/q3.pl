
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
start('G5').

end('G17').

% Returns whether there is an edge between X and Y.
edge(X,Y):- weights(X,Y,_).

undirected_edge(X,Y):- weights(X,Y,_).
undirected_edge(X,Y):- weights(Y,X,_).

is_empty([]).

check_path(List):-
	[H|T] = List,
	is_empty(T),
	end(H).

check_path(List):-
	[X1,X2|_] = List,
	undirected_edge(X1,X2),
	[_|T] = List,
	check_path(T).

valid(List):-
	[H|_] = List,
	start(H),
	check_path(List),
	write("Path Found"),
	nl.

valid(List):-
	write("Path Not Found"),
	nl.












