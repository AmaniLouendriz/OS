#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <sys/shm.h>
#include <sys/stat.h>
#include <sys/mman.h>

int main(){
    const char *name = "Catalan Sequence";
    const int SIZE = 10000;

    int shm_fd;
	void *ptr;

    /* Opening the shared memory segment */
	shm_fd = shm_open(name, O_RDONLY, 0666);

    if (shm_fd == -1) {
		printf("shared memory failed\n");
		exit(-1);
	}

    /* Mapping the shared memory segment in the address space of the process */
	ptr = mmap(0,SIZE, PROT_READ, MAP_SHARED, shm_fd, 0);


    if (ptr == MAP_FAILED) {
		printf("Map failed\n");
		exit(-1);
	}


    /* Reading from the shared memory region */
	printf("%s",(char *)ptr);

	/* Removing the shared memory segment */
	if (shm_unlink(name) == -1) {
		printf("Error removing %s\n",name);
		exit(-1);
	}

	return 0;



}