%initializing the menu
starter('Corn Tikki', 30).
starter('Tomato Soup', 20).
starter('Chilli Paneer', 40).
starter('Crispy Chicken', 40).
starter('Papdi Chaat', 20).
starter('Cold Drink', 20).

main('Kadhai Paneer with Butter/Plain Naan', 50).
main('Veg Korma with Butter/Plain Naan', 40).
main('Murgh Lababdar with Butter/Plain Naan', 50).
main('Veg Dum Biryani with Dal Tadka', 50).
main('Steam Rice with Dal Tadka', 40).

dessert('Ice-cream', 20).
dessert('Malai Sandwich', 30).
dessert('Rasmalai', 10).
% initializing all possible combinations for diet,hungry,not_so_hungry
check_diet(1,0,0) :- valid_diet(1,0,0).
check_diet(0,1,0) :- valid_diet(0,1,0).
check_diet(0,0,1) :- valid_diet(0,0,1).

check_hungry(1,1,1) :- valid_hungry(1,1,1).

check_not_so_hungry(1,1,0) :- valid_not_so_hungry(1,1,0).
check_not_so_hungry(0,1,1) :- valid_not_so_hungry(0,1,1).

% check for validity of hungry
valid_hungry(X,Y,Z) :-
    (X=:=1,
     Y=:=1,
     Z=:=1,
     starter(_,A),
     main(_,B),
     dessert(_,C),
     A+B+C=<80
).

% check for validity of diet
valid_diet(X,Y,Z) :-
    (X+Y+Z=:=1,
     (X=:=1,main(_,A),A=<40;
     Y=:=1,starter(_,A),A=<40;
     Z=:=1,dessert(_,A),A=<40)
).

% check for validity of not so hungry
valid_not_so_hungry(X,Y,Z) :-
    (Y=:=1,
     X+Z=:=1,
     ((X=:=1, starter(_,A),main(_,B),A+B=<80);
     (Z=:=1, main(_,A),dessert(_,B),A+B=<80))
).

% printing valid diet combination
valid_find_diet(X,Y,Z):-
    (  X=:=1,starter(B,A),A=<40,write("Items: "),write(B),nl;
       Y=:=1,main(B,A),A=<40,write("Items: "),write(B),nl;
       Z=:=1,dessert(B,A),A=<40,write("Items: "),write(B),nl
),false().

% printing valid hungry combination
valid_find_hungry(X,Y,Z):-
    (   starter(D,A),
        main(E,B),
        dessert(F,C),
        A+B+C=<80,
        write("Items: "),write(D),write(", "),
        write(E),write(", "),
        write(F),nl
),false().

% printing valid not so hungry combination
valid_not_so_hungry(X,Y,Z):-
    (   X=:=1, starter(D,A),main(E,B),write("Items: "), write(D),write(", "),write(E),nl;
        Z=:=1, main(D,A), dessert(E,B),write("Items: "),write(D),write(", "),write(E),nl
),false().

% initializing values for finding combinations of meal
find_diet(1,0,0) :- valid_find_diet(1,0,0).
find_diet(0,1,0) :- valid_find_diet(0,1,0).
find_diet(0,0,1) :- valid_find_diet(0,0,1).

find_hungry(1,1,1) :- valid_find_hungry(1,1,1).

find_not_so_hungry(1,1,0) :- valid_find_not_so_hungry(1,1,0).
find_not_so_hungry(0,1,1) :- valid_find_not_so_hungry(0,1,1).

% checking for valid combination
menu(Status,X,Y,Z) :-
    (Status = 'diet' -> check_diet(X,Y,Z);
     Status = 'hungry' -> check_hungry(X,Y,Z);
     Status = 'not_so_hungry' -> check_not_so_hungry(X,Y,Z)
).

% finding valid combination
find_items(Status,X,Y,Z) :-
    (Status = 'diet' -> find_diet(X,Y,Z);
     Status = 'hungry' -> find_hungry(X,Y,Z);
     Status = 'not_so_hungry' -> find_not_so_hungry(X,Y,Z)
 ).













