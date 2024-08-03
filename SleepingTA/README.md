# The Sleeping Teaching Assistant

This program is a simulation for a casual university situation, where a TA is helping students. It uses principally the power of threads and semaphores to simulate students as threads, and the TA as another thread. Interaction between the two entities is protected by semaphores in different critical conditions.

Students (threads) altern between programming themselves and between asking the TA (another thread) for help. If the TA is free, they will be helped. Otherwise, they will have to sit on a chair in the hallway. If no chair is free, then they will continue doing their work and they will come back at a later time. If a student comes and TA  is sleeping, TA should be notified using a semaphore.

When the TA is done helping students, he should go and see whether students are still waiting in the hallway, if yes, TA should help 
each one of the students. If no student are there, TA returns sleeping.

There are just three chairs outside the hallway.