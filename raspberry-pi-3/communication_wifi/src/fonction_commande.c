/**
 *	\file	fonction_commande.c
 *	\biref	fonction necessaire à la communication wifi
 *	\authors Irian.J Nicolas.D
 *	\version 1.0
 *	\date 22 Septembre 2017
 *
 */



#include<routage_commande.h>

void die(char *s)
{
    perror(s);
    exit(1);
}
