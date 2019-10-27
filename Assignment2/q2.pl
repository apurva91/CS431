%initializing the menu
starter('Corn Tikki', 30,1).
starter('Tomato Soup', 20,2).
starter('Chilli Paneer', 40,3).
starter('Crispy Chicken', 40,4).
starter('Papdi Chaat', 20,5).
starter('Cold Drink', 20,6).


main('Kadhai Paneer with Butter/Plain Naan', 50,1).
main('Veg Korma with Butter/Plain Naan', 40,2).
main('Murgh Lababdar with Butter/Plain Naan', 50,3).
main('Veg Dum Biryani with Dal Tadka', 50,4).
main('Steam Rice with Dal Tadka', 40,5).

dessert('Ice-cream', 20,1).
dessert('Malai Sandwich', 30,2).
dessert('Rasmalai', 10,3).

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
     starter(_,A,_),
     main(_,B,_),
     dessert(_,C,_),
     A+B+C=<80
).

% check for validity of diet
valid_diet(X,Y,Z) :-
    (X+Y+Z=:=1,
     (X=:=1,main(_,A,_),A=<40;
     Y=:=1,starter(_,A,_),A=<40;
     Z=:=1,dessert(_,A,_),A=<40)
).

% check for validity of not so hungry
valid_not_so_hungry(X,Y,Z) :-
    (Y=:=1,
     X+Z=:=1,
     ((X=:=1, starter(_,A,_),main(_,B,_),A+B=<80);
     (Z=:=1, main(_,A,_),dessert(_,B,_),A+B=<80))
).

isEmpty([]):- true.

% Apply knapsack on items.

take_starters(_,7,[]):- fail.


take_starters(_,7,Take):-
    \+ isEmpty(Take),
    atomic_list_concat(Take,', ',String),
    write("Items:- "),
    write(String),
    nl,fail.

take_starters(Sum,Index,Take):-
    \+ Index =:= 7,
    starter(B,A,Index),
    NewIndex is Index+1,
    (
        (
            A =< Sum,
            NewSum is Sum - A,
            take_starters(NewSum,NewIndex,[B|Take])
        );
        take_starters(Sum,NewIndex,Take)
    ).

take_main_dish(_,6,[]):- fail.


take_main_dish(_,6,Take):-
    \+ isEmpty(Take),
    atomic_list_concat(Take,', ',String),
    write("Items:- "),
    write(String),
    nl,fail.

take_main_dish(Sum,Index,Take):-
    \+ Index =:= 6,
    main(B,A,Index),
    NewIndex is Index+1,
    (
        (
            A =< Sum,
            NewSum is Sum - A,
            take_main_dish(NewSum,NewIndex,[B|Take])
        );
        take_main_dish(Sum,NewIndex,Take)
    ).

take_dessert(_,4,[]):- fail.


take_dessert(_,4,Take):-
    \+ isEmpty(Take),
    atomic_list_concat(Take,', ',String),
    write("Items:- "),
    write(String),
    nl,fail.

take_dessert(Sum,Index,Take):-
    \+ Index =:= 4,
    dessert(B,A,Index),
    NewIndex is Index+1,
    (
        (
            A =< Sum,
            NewSum is Sum - A,
            take_dessert(NewSum,NewIndex,[B|Take])
        );
        take_dessert(Sum,NewIndex,Take)
    ).

 

% printing valid diet combination
valid_find_diet(X,Y,Z):-
    (  X=:=1,take_starters(40,1,[]);
       Y=:=1,take_main_dish(40,1,[]);
       Z=:=1,take_dessert(40,1,[])
),false().

% printing valid hungry combination
valid_find_hungry(X,Y,Z):-
    (   starter(D,A,_),
        main(E,B,_),
        dessert(F,C,_),
        A+B+C=<80,
        write("Items: "),write(D),write(", "),
        write(E),write(", "),
        write(F),nl
),false().

% printing valid not so hungry combination
valid_find_not_so_hungry(X,Y,Z):-
    (   X=:=1, starter(D,A,_),main(E,B,_),A+B=<80,write("Items: "), write(D),write(", "),write(E),nl;
        Z=:=1, main(D,A,_), dessert(E,B,_),A+B=<80,write("Items: "),write(D),write(", "),write(E),nl
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