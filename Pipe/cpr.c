/*------------------------------------------------------------
Fichier / File: cpr.c

Nom / Name: Amani Louendriz

Description: 


	This program contains the code to create n child processes and attach a pipe to each child process. 
	The child processes would then use the pipes to send messages that will eventually end being displayed on the 
	standard output.

	Ce programme contient le code pour la creation
	d'un processus enfant et y attacher un tuyau.
	L'enfant envoyera des messages par le tuyau
	qui seront ensuite envoyes a la sortie standard.


Explication du processus zombie
(point 5 de "A completer" dans le devoir):

	(s.v.p. completez cette partie);

	Apres l'analyse des processus lancés, je remarque qu'un processus est nommé ``defunct``; ceci veut
	dire qu'il représente un processus zombie qui a terminé son exécution. Mais il est toujours présent 
	dans la table des processus. Je pense que ce processus est le cas de base de l'appel récursive. En effet,
	ce processus finit effectivement son exécution à un certain point puisqu'il y a un return à la fin. Mais son status
	de exit n'est pas collecté par son parent.. Les processus créent des processus enfants en faisant l`appel récursive
	mais n'attendent pas explicitement la fin de l'exécution de leurs enfants avec des appels systèmes comme le wait.

	Les enfants aussi ne performent pas des appels systèmes comme le exit pour informer les parents de leurs status. Dance ce cas, on s'assure que l'enfant
	a terminé l'exécution par le fait qu'il a fermé son bout d'écriture dans le tuyau. Ce qui ne donne pas d'information 
	à son parent de son état.

-------------------------------------------------------------*/
#include <stdio.h>
#include <sys/select.h>
#include <unistd.h>
#include <string.h>


#include <sys/wait.h>



#define READ_END 0
#define WRITE_END 1

#define BUFFER_SIZE 1024




/* Prototype */
void creerEnfantEtLire(int );

/*-------------------------------------------------------------
Function: main
Arguments: 
	int ac	- nombre d'arguments de la commande
	char **av - tableau de pointeurs aux arguments de commande
Description:
	Extrait le nombre de processus a creer de la ligne de
	commande. Si une erreur a lieu, le processus termine.
	Appel creerEnfantEtLire pour creer un enfant, et lire
	les donnees de l'enfant.
-------------------------------------------------------------*/

int main(int ac, char **av)
{
    int numeroProcessus; 

    if(ac == 2)
    {
       if(sscanf(av[1],"%d",&numeroProcessus) == 1)
       {
           creerEnfantEtLire(numeroProcessus);
       }
       else fprintf(stderr,"Ne peut pas traduire argument\n");
    }
    else fprintf(stderr,"Arguments pas valide\n");
    return(0);
}


/*-------------------------------------------------------------
Function: creerEnfantEtLire
Arguments: 
	int prcNum - le numero de processus
Description:
	Cree l'enfant, en y passant prcNum-1. Utilise prcNum
	comme identificateur de ce processus. Aussi, lit les
	messages du bout de lecture du tuyau et l'envoie a 
	la sortie standard (df 1). Lorsqu'aucune donnee peut
	etre lue du tuyau, termine.
-------------------------------------------------------------*/

void creerEnfantEtLire(int prcNum)
{

	    if(prcNum == 1){
			fprintf(stdout,"Processus %d commence.\n",prcNum);
			sleep(5);
			fprintf(stdout,"Processus %d termine.\n",prcNum);
			return;
	    }

		// Si on est ici, c a d on va creer un enfant

		int p[2];
		int pid;

		if(pipe(p) == -1){
			fprintf(stderr,"Erreur lors de la creation de la pipe.");
			return;
		}

		char write_msg1[BUFFER_SIZE];// le tableau dans lequel on ecrit le message du commencement/
		char write_msg2[BUFFER_SIZE];// le tableau dans lequel on ecrit le message de fin /

		pid = fork();

		if(pid < 0){
			fprintf(stderr,"Erreur lors du fork.");
			return;
		}

		if (pid == 0){
			// ici c l enfant

			// au debut, ce processus sera le processus 4

			close(p[READ_END]);

			int newPrcNum = prcNum - 1;// decrementer le num de processus

			char newPrcNumChaine[12];// initialise une chaine de 12
			sprintf(newPrcNumChaine,"%d",newPrcNum);// convertir le int en chaine


			dup2(p[WRITE_END],1);// attacher le bout ecrivant de tuyau a la sortie standart de l'enfant

			write(p[WRITE_END],"",strlen(write_msg2)+1);

			close(p[WRITE_END]);


			execlp("./cpr","./cpr",newPrcNumChaine,NULL);
		}else if(pid > 0){
			// ici c'est le parent

			// au debut (premiere iteration), il sera le processus 5.

			close(p[WRITE_END]);
		
			char read_msg[BUFFER_SIZE];

			sprintf(write_msg1,"Processus %d commence.\n",prcNum);
			fprintf(stdout,"%s",write_msg1);



        	while ((read(p[READ_END], read_msg, BUFFER_SIZE)) > 0) {
            	fprintf(stdout,"%s", read_msg);
            	memset(read_msg, 0, sizeof(read_msg)); // Effacer le buffer apres chaque lecture
        	}

			sleep(5);

			sprintf(write_msg2,"Processus %d termine.\n",prcNum);
			fprintf(stdout,"%s",write_msg2);

			close(p[READ_END]);

			sleep(10);
		}	    
}
