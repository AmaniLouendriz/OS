#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <fcntl.h>
#include <sys/shm.h>
#include <sys/stat.h>
#include <sys/mman.h>
#include <unistd.h>


/*Function prototype: */

int createSharedMemory(int);
unsigned long int* calculateCatalan(unsigned long int*,int);
unsigned long int factoriel(int);

int main(int ac,char **av){
    int sequenceNumber;

    if(ac == 2){
        if(sscanf(av[1],"%d",&sequenceNumber) == 1){
            if(sequenceNumber < 0){
                fprintf(stderr,"Please provide a positive number.");
                return -1;
            }

            if(sequenceNumber >= 11){
                fprintf(stderr,"Please note that due to the exponential behavior of factorial; such numbers may give inappropriate results. please try lower.");
                return -1;
            }
            createSharedMemory(sequenceNumber);

        }else{
            fprintf(stderr,"Char to int failure!");
        }
    }else{
        fprintf(stderr,"Please input a valid number at args[1]");
    }

    return 0;
}

int createSharedMemory(int sequenceNumber){
    const int SIZE = 4096;// size of the shared memory object
	const char *name = "Catalan Sequence";// name of the shared memory space.

    int shm_fd;
	void *ptr;

    /* Creating the shared memory segment */
	shm_fd = shm_open(name, O_CREAT | O_RDWR, 0666);

    /* Configuring the size of the shared memory segment */
	ftruncate(shm_fd,SIZE);

    /* Mapping the shared memory segment in the address space of the process */
	ptr = mmap(0,SIZE, PROT_READ | PROT_WRITE, MAP_SHARED, shm_fd, 0);

    if (ptr == MAP_FAILED) {
		printf("Map failed\n");
		return -1;
	}

    // here we do the calculations

    unsigned long int res[sequenceNumber];

    calculateCatalan(res,sequenceNumber);

    /*Writing to the shared memory after the calculations */

    int length;

    for(int i = 0; i< sequenceNumber;i++){
        length = sprintf(ptr,"catalanSequence[%d] is : %lu \n",i,res[i]);
        //printf("%d \t",length);
	    ptr += length;
    }

    fprintf(stdout,"The sequence has been written to the shared memory object. Please run the consumer to see the outcome.");

    return 0;
}

unsigned long int* calculateCatalan(unsigned long int *res,int sequenceNumber){
    unsigned long int catalanNumber;
    //printf("catalanNumberEx %ld \n", catalanNumber);
    unsigned long int num,den;
    for (int i = 1; i <= sequenceNumber;i++){
        //printf("factor(%d)=  %ld \n",i,factoriel(i));
        num = factoriel(2*i);
        den = factoriel(i+1)*factoriel(i);
        catalanNumber = num/den;
        //printf("factoriel(%d) num: %ld \n",i,num);
        //printf("factoriel(%d) den: %ld \n",i,den);

        res[i-1] = catalanNumber;
    }
    return res;
}

unsigned long int factoriel(int n){
    unsigned long int factoriel = 1;
    for(int i = 1;i<=n;i++){
        factoriel = factoriel*i;

    }

    return factoriel;
}