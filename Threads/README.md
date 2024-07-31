dev2Q1V2 and dev2Q1V2Worker implements a solution to compute prime numbers less than a certain number entered by the user.
A reasonable amount of threads are started to cover a portion of the input space:\[2,n\], and they all share the same container where they
put what was found.


dev2Q2V2 and dev2Q2V2Worker implement a similar solution but to compute fibonacci suite. A reasonable amount of threads are started in 
parallel and they cooperate together by using required mechanisms.