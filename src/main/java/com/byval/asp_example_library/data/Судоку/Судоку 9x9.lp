% ---СУДОКУ 9X9---
% Дано: игровое поле размером 9x9
% Цель: заполнить свободные клетки цифрами от 1 до 9 так,
% чтобы каждая цифра встречалась бы ровно один раз в каждой
% строке, каждом столбце и в каждом малом квадрате 3×3.

% Начальные значения в клетках
x(1,4,7).x(1,5,9).
x(2,3,5).x(2,4,8).x(2,6,1).x(2,7,6).x(2,8,7).
x(3,1,3).x(3,6,6).
x(4,2,9).x(4,3,3).x(4,5,6).x(4,7,5).
x(5,1,6).x(5,6,5).x(5,7,3).x(5,9,1).
x(6,6,9).x(6,9,4).
x(7,1,7).x(7,4,1).x(7,6,3).x(7,7,8).
x(9,4,6).x(9,6,8).

%Возможные значения в клетках
val(1..9).

%Границы групп клеток
border(1;4;7).

%Проверяем группу клеток
1{ x(X,Y,N): val(X), val(Y), X1<=X, X<= X1+2, Y1<=Y, Y<=Y1+2}1:- val(N), border(X1), border(Y1).

%Проверка по строкам и столбцам
1{ x(X,Y,N): val(N)}1 :- val(X), val(Y).
1{ x(X,Y,N) : val(X)}1 :- val(N), val(Y).
1{ x(X,Y,N) : val(Y)}1 :- val(N), val(X).
#show x/3.