import java.util.ArrayList;

import java.util.Random;

import java.util.Queue;
import java.util.LinkedList;
import java.util.Arrays;

import java.io.*;

/***
 * Amani Louendriz
 * 
 */

public class Algorithm{

    public static void main (String[] args){
        int n = 10;// longueur de la chaine de reference
        Random random = new Random();

        ArrayList<Integer> chaineDeReference = new ArrayList<>();

        for (int i = 0; i < n; i++){// ajouter les nombres aleatoires a la chaine de reference
            int number = random.nextInt(0,n);
            chaineDeReference.add(number);
        }

        //imprimer la chaine de reference

        System.out.println("\n The generated reference string: "+chaineDeReference);

        int longueurDeCadreDePage = random.nextInt(1,8);


        ArrayList<Integer> cadreDePagesFifo = new ArrayList<>(longueurDeCadreDePage);

        ArrayList<Page> cadreDePagesLRU = new ArrayList<>(longueurDeCadreDePage);// Dans le lru on a besoin de savoir la date de referencement
        //cest pour cela que l'array ici est constitue de Page Object.

        System.out.println("The length of generated page frame is "+ longueurDeCadreDePage + "\n");


        int pageFaultFifo = Fifo(chaineDeReference,cadreDePagesFifo,longueurDeCadreDePage);
        int pageFaultLRU = LRU(chaineDeReference,cadreDePagesLRU,longueurDeCadreDePage);



       System.out.println("After FIFO: "+ cadreDePagesFifo);
       System.out.print("After LRU:  ");

       for (int i = 0;i<cadreDePagesLRU.size();i++){
         System.out.print(cadreDePagesLRU.get(i).getValue() + "\t");
       }


       System.out.println("\n # Page faults FIFO: "+ pageFaultFifo);
       System.out.println("# Page faults LRU: "+ pageFaultLRU);

       System.out.println("Les fautes de pages initiaux sont egalement pris en compte dans la valeur de faute de page a la fin. i.e: le moment ou on place une page dans un cadre vide.");

    }

    /***
     * Fifo est la methode qui va remplir le tableau de pages selon le principe Fifo.
     * 
     * chaineDeReference: la chaine qui contient les differentes sequences de pages souhaites.
     * tableauDePage: tableau de page disponible; une liste d'entiers
     * longueurDeCadreDePage: la taille maximale de tableauDePage; qui apres l'avoir atteint, il faut remplacer les pages victimes.
     * 
     * Les fautes de pages initiaux sont egalement pris en compte dans la valeur de faute de page a la fin. i.e: le moment ou on place une page 
     * dans un cadre vide.
     * 
     */

    public static int Fifo(ArrayList<Integer> chaineDeReference,ArrayList<Integer> tableauDePage,int longueurDeCadreDePage){

        Queue<Integer> queue = new LinkedList<>();// pour choisir qui sera la victime

        int defaultPage = 0;

        for(int i = 0;i < chaineDeReference.size();i++){// iterer a travers la chaine de reference

           int value = chaineDeReference.get(i);// page a etudier

           if(!tableauDePage.contains(value)){

                if(tableauDePage.size() < longueurDeCadreDePage){
                    tableauDePage.add(value);
                    queue.offer(value);
                    defaultPage++;
                }else{// tableauDePage est plein

                    int victime = queue.poll();

                    int indexOfVictim = tableauDePage.indexOf(victime);

                    tableauDePage.set(indexOfVictim,value);

                    queue.offer(value);
                    defaultPage++;



                }
           }
        }
        return defaultPage;
    }

    /**
     LRU est la methode qui va remplir le tableau de pages selon le principe LRU.
     * 
     * chaineDeReference: la chaine qui contient les differentes sequences de pages souhaites.
     * 
     * tableauDePage: liste de pages disponibles; une liste de Page Object, vu que LRU fonctionne en prenant comme victime la page 
     * qui a le temps de referencement le plus petit(plus lointain)
     * 
     * longueurDeCadreDePage: la taille maximale de tableauDePage
     * 
     * LRU fonctionne en prenant comme victime la page 
     * qui a le temps de referencement le plus petit; dans cette methode, pour visualiser ceci; au lieu de prendre le timeStamp comme etant la valeur 
     * de temps actuel; timeStamp etait pris comme etant un entier flag. flag est incrementee a chaque fois donc ceci assure que les flags (timeStamps)
     * des pages sont toutes distincts. i.e. Il n y aura pas deux pages qui auront le meme timeStamp (flag). Cette incrementation pourrrait 
     * etre visualisee comme etant l'incrementation que subit normalement une valeur temporelle en ms par exemple.
     * 
     * Les fautes de pages initiaux sont egalement pris en compte dans la valeur de faute de page a la fin. i.e: le moment ou on place une page 
     * dans un cadre vide.
     */

    public static int LRU(ArrayList<Integer> chaineDeReference,ArrayList<Page> tableauDePage,int longueurDeCadreDePage){

        int defaultPage = 0;// compteur qui compte le nombre de fautes de Page, incluant ceux initiaux ou on place les pages pour la premiere fois
        int flag = 0;

        for(int i = 0;i < chaineDeReference.size();i++){// iterer a travers la chaine de reference

           int value = chaineDeReference.get(i);// page a etudier

           if (tableauDePage.size() == 0){
                flag++;
                Page page = new Page(value,flag);
                tableauDePage.add(page);
                defaultPage++;
           }else if(!doesArrayContain(tableauDePage,value)){// il y a pas la valeur dans le tableau

                if(tableauDePage.size() < longueurDeCadreDePage){
                    flag++;
                    Page page = new Page(value,flag);
                    tableauDePage.add(page);
                    defaultPage++;
                }else{// le tableau est plein

                    // choisir celui qui a la plus petite valeur de timeStamp
                    flag++;

                    Page p = getSmallestTimeStamp(tableauDePage);
                    int indexOfVictim = tableauDePage.indexOf(p);

                    Page page = new Page(value,flag);

                    tableauDePage.set(indexOfVictim,page);
                    defaultPage++;
                }
           }else{
                // update the time stamp here

                flag++;

                Page p = getPageValue(tableauDePage,value);
                p.setTimeStamp(flag);
           }

          
        }

        return defaultPage;

    }

    /**
     * 
     * Cette helper methode va voir si dans le tableau, il y a deja la valeur que l'on souhaite
     */

    private static boolean doesArrayContain(ArrayList<Page> array,int value){
        boolean doesContain = false;
        for(int i = 0; i < array.size();i++){
            Page p = array.get(i);
            if(p.getValue() == value){
                doesContain = true;
                break;
            }
        }
        return doesContain;
    }

    /**
     * 
     * Cette helper method va nou donner le Page object dans le tableau de Page en se basant sur la valeur de la page (int)
     * 
     * 
     */


    private static Page getPageValue(ArrayList<Page> array,int value){


        for (int i = 0;i<array.size();i++){
            Page p = array.get(i);

            if(p.getValue() == value){
                return p;

            }
        }
        return null;
    }


    /**
     * Cette helper method va nous donner le page object qui a la valeur specifique de timeStamp
     * 
     */

    private static Page getPageFlag(ArrayList<Page> array,int flag){
        for (int i = 0;i<array.size();i++){
            Page p = array.get(i);

            if(p.getTimeStamp() == flag){
                return p;
            }
        }
        return null;
    }

    /**
     * Cette helper method va nous donner la page qui a la plus petite valeur de timeStamp
     * 
     */

    private static Page getSmallestTimeStamp(ArrayList<Page> array){

        int minTimeStamp = Integer.MAX_VALUE;

        for (int i = 0; i<array.size();i++){
            Page p = array.get(i);

            if(p.getTimeStamp() < minTimeStamp){
                minTimeStamp = p.getTimeStamp();
            }

        }

        Page foundPage = getPageFlag(array,minTimeStamp);

        return foundPage;
    }
}