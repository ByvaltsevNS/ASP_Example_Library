% ---РАСКРАШИВАНИЕ НЕОРИНТИРОВАННОГО ГРАФА---
% Дано: неориентированный граф
% Цель: раскрасить вершины графа тремя разными 
% цветами таким образом, чтобы никакие две смежные 
% вершины не должны быть окрашены в одинаковый цвет

%Вершины
node(1..10).

%Зададим дуги
edge(1,(2;5;7)).  
edge(2,(1;3;8)).   
edge(3,(2;4;9)). 
edge(4,(3;5;10)).
edge(5,(1;4;6)).
edge(6,(5;8;9)).
edge(7,(1;9;10)).
edge(8,(2;6;10)).
edge(9,(3;6;7)).
edge(10,(4;7;8)).

%Зададм цвета
color(red). color(green). color(blue).

% Делаем неориентированный граф: если между между X
%и Y есть вершина, то и между Y и X она есть
edge(Y,X) :- edge(X,Y).

%Определяем каждой вершине только один цвет
1 {colors(X,C) : color(C)} 1 :- node(X).

%Проверка цветов соседних вершин
:- edge(X,Y), colors(X,C), colors(Y,C).
#show colors/2.


