/**
 *	\file	uart.c
 *	\brief	programme main 
 *	\authors	Irian.J Nicolas.D
 *	\version	2.0
 *	\date	22 Septembre 2017
 *
 *	Programme de communication de l'UART
 *
 */


#include "uart.h"


/**
 *	\fn	int main (void)
 *	\brief Communication UART fonctionnelle
 *	
 *	\return 1 - Non utilisé 
 */ 
int main(void) 
{

	int uart0_filestream = -1;
	
	uart0_filestream = open("/dev/ttyAMA0", O_RDWR);		//Open in non blocking read/write mode
	if (uart0_filestream == -1)
	{
		//ERROR - CAN'T OPEN SERIAL PORT
		printf("Error - Unable to open UART.  Ensure it is not in use by another application\n");
	}

	struct termios options;
	tcgetattr(uart0_filestream, &options);
	options.c_cflag = B9600 | CS8 | CLOCAL | CREAD;		//<Set baud rate
	options.c_iflag = IGNPAR;
	options.c_oflag = 0;
	options.c_lflag = 0;
	tcflush(uart0_filestream, TCIFLUSH);
	tcsetattr(uart0_filestream, TCSANOW, &options);

    /* Affichage de l'acceuil */
	unsigned char* message_acceuil =
"\r\n*******  *****   ****  *******  ****\r\n*  ****  *    *  *  *  *  ****  ****\r\n*  *     *  *  * *  *  *  *     ****\r\n*  ***   *  **  **  *  *  ****  *  *\r\n*  ***   *  * *  *  *  ****  *  *  *\r\n*  *     *  *  *    *     *  *  *  *\r\n*  ****	 *  *   *   *  ****  *  *  *\r\n*******  ****    ****  *******  ****\r\nSATEFARI - Supervison Processor Interface\r\nEnter help to know the supported commands";
	unsigned char* help ="speed     : update speed value\r\nsteering  : update steering value\r\nmeas      : print current - voltage power in real time.\r\nPress enter to quit";
	
	unsigned char* ask_speed = "Enter value between 0 (stop) and 1023 (full speed) :";
	unsigned char* ask_steering = "Enter value between -127 (full left) and 128 (full right), 0 is the middle position :";
	unsigned char* ask_meas = "currents and voltage analog values for propulsion module : \n\r"; 
	unsigned char* error_speed ="error : failed value";
	unsigned char* succed_speed = "change speed succeded";
	unsigned char* succed_steering = "change steering succeded";
	unsigned char* error_command = "Error : command not supported. Enter help to know the supported commands";
	
	/* Adresse de l'esclave pour la prochaine communication I2C */	
	int addr = 0x20;
	int length = 9; 
	int file_i2c;

	unsigned char buffer[9];

	unsigned char* command_line = "\r\ncontrol:~# ";
	unsigned char* retour_chariot = "\r\n";
	unsigned char* buffer_reception[256];
	unsigned char rx_buffer[256];

	unsigned char save_speed[250]= "0000";
	unsigned char save_steering[250]="+000";

	unsigned char* help_test = "help\r";
	unsigned char* speed_test = "speed\r";
	unsigned char* steering_test = "steering\r";
	unsigned char* meas_test = "meas\r";

	int speed_int;
	int steering_int;

	char *endptr; 
	
	affichage(uart0_filestream,message_acceuil,strlen(message_acceuil));
	
	while (1) {
							
		affichage(uart0_filestream,command_line,strlen(command_line));
		receive(uart0_filestream,rx_buffer); 
		affichage(uart0_filestream,retour_chariot,strlen(retour_chariot));
		
		if ( !strcmp(rx_buffer,help_test) ) 
		{		
			affichage(uart0_filestream,help,strlen(help));
			receive(uart0_filestream,rx_buffer);
		}

		else if ( !strcmp(rx_buffer,speed_test) )
		{
			affichage(uart0_filestream,ask_speed,strlen(ask_speed));
			affichage(uart0_filestream,retour_chariot,strlen(retour_chariot));
			receive(uart0_filestream,rx_buffer);
			affichage(uart0_filestream,retour_chariot,strlen(retour_chariot));
			
			speed_int = strtol( rx_buffer, &endptr, 10);

			if (endptr == rx_buffer) 
			{
				affichage(uart0_filestream,error_speed,strlen(error_speed));
			}
			else if ( (speed_int <= 1023) && (speed_int >=0) ) 
			{
					
				affichage(uart0_filestream,succed_speed,strlen(succed_speed));
				affichage(uart0_filestream,retour_chariot,strlen(retour_chariot));

				if (speed_int < 10) 
				{
					
					buffer[0] = '0'; 	
					buffer[1] = '0';	
					buffer[2] = '0';
					*(buffer+3) = rx_buffer[0];
					buffer[4] = ' '; 	
					*(buffer+5) = save_steering[0];	
					*(buffer+6) = save_steering[1];
					*(buffer+7) = save_steering[2];
					*(buffer+8) = save_steering[3];
					buffer[9] = '\0';
				}

				else if (speed_int < 100)
				{	
					
					buffer[0] = '0';	
					buffer[1] = '0';					 
					*(buffer+2) = rx_buffer[0];
					*(buffer+3) = rx_buffer[1];
					buffer[4] = ' ';
					*(buffer+5) = save_steering[0];	
					*(buffer+6) = save_steering[1];
					*(buffer+7) = save_steering[2];
					*(buffer+8) = save_steering[3];
					buffer[9] = '\0';
				}

				else if (speed_int < 1000) 
				{
					
					buffer[0] = '0';
					*(buffer+3) = rx_buffer[2];
					*(buffer+2) = rx_buffer[1];
					*(buffer+1) = rx_buffer[0];	
					buffer[4] = ' '; 	
					*(buffer+5) = save_steering[0];	
					*(buffer+6) = save_steering[1];
					*(buffer+7) = save_steering[2];
					*(buffer+8) = save_steering[3];
					buffer[9] = '\0';
				}

				else 
				{
					*(buffer+3)   = rx_buffer[3];
					*(buffer+2) = rx_buffer[2];
					*(buffer+1) = rx_buffer[1];
					*(buffer) = rx_buffer[0];	
					buffer[4] = ' '; 	
					*(buffer+5) = save_steering[0];	
					*(buffer+6) = save_steering[1];
					*(buffer+7) = save_steering[2];
					*(buffer+8) = save_steering[3];
					buffer[9] = '\0';					
				}	

				*(save_speed+0) = buffer[0];
				*(save_speed+1) = buffer[1];
				*(save_speed+2) = buffer[2];
				*(save_speed+3) = buffer[3];
				affichage(uart0_filestream,buffer,strlen(buffer));


				/* Envoie de la commande de propulsion par i2c */
					
				/* Ouverture du bus I2C */
				char *filename = (char*)"/dev/i2c-1";
				if ((file_i2c = open(filename, O_RDWR)) < 0)
				{
						/* Erreur à l'ouverture du bus, vérifier errno pour plus détails */
						printf("Failed to open the i2c bus");
				}
				/* Début de la communication avec l'esclave */
				addr = 0x20;
				if (ioctl(file_i2c, I2C_SLAVE, addr) < 0)
				{
					printf("Failed to acquire bus access and/or talk to slave.\n");
					/* Erreur à la communication avec l'esclave, voir errno pour plus de détails */
				}
				/* Ecriture sur le bus I2C */	
				/* Longueur de la transmission à effectuer - doit correspondre à la tailel du buffer (en octet) */
				length = 9;		
				/* Write retourne le nombre d'octet écris, une erreur apparaît s'il ne correspond pas à la demande envoyée */ 
				if (write(file_i2c, buffer, length) != length)		
				{
					/* Affichage d'une erreur de communication */
					printf("Failed to write to the i2c bus.\n");
				}		
				
			}

			else 
			{
				affichage(uart0_filestream,error_speed,strlen(error_speed));
			}
		}

		else if ( !strcmp(rx_buffer,steering_test) )
		{

			affichage(uart0_filestream,ask_steering,strlen(ask_steering));
			affichage(uart0_filestream,retour_chariot,strlen(retour_chariot));
			receive(uart0_filestream,rx_buffer);
			affichage(uart0_filestream,retour_chariot,strlen(retour_chariot));

			steering_int = strtol( rx_buffer, &endptr, 10);
			
			if (endptr == rx_buffer) 
			{
				affichage(uart0_filestream,error_speed,strlen(error_speed));
			}
			else if ( ( steering_int <= 128 ) && (steering_int >= -127) ) 
			{
				affichage(uart0_filestream,succed_steering,strlen(succed_steering));
				affichage(uart0_filestream,retour_chariot,strlen(retour_chariot));
				
    			if ( (steering_int < -9) && (steering_int > -100) )
				{

					*(buffer+0) = save_speed[0];
					*(buffer+1) = save_speed[1];
					*(buffer+2) = save_speed[2];
					*(buffer+3) = save_speed[3];
					buffer[4] = ' ';	
					*(buffer+5)	= rx_buffer[0];
					buffer[6] = '0';
					*(buffer+7) = rx_buffer[1];	
					*(buffer+8)	= rx_buffer[2];
					buffer[9] = '\0';
				}

				else if (   (steering_int > -10) && (steering_int < 0 ) )  
				{
					
					*(buffer+0) = save_speed[0];
					*(buffer+1) = save_speed[1];
					*(buffer+2) = save_speed[2];
					*(buffer+3) = save_speed[3];
					buffer[4] = ' ';	
					*(buffer+5)	= rx_buffer[0];
					buffer[6] = '0';
					buffer[7] = '0';
					*(buffer+8)	= rx_buffer[1];
					buffer[9] = '\0';

				}
				
				else if (   (steering_int > -1) && (steering_int < 10 ) ) 
				{

					*(buffer+0) = save_speed[0];
					*(buffer+1) = save_speed[1];
					*(buffer+2) = save_speed[2];
					*(buffer+3) = save_speed[3];
					buffer[4] = ' ';	
					buffer[5] = '+';
					buffer[6] = '0';
					buffer[7] = '0';
					*(buffer+8)	= rx_buffer[0];
					buffer[9] = '\0';

				} 				
				else if (   (steering_int > 9) && (steering_int < 100 ) )
				{
					*(buffer+0) = save_speed[0];
					*(buffer+1) = save_speed[1];
					*(buffer+2) = save_speed[2];
					*(buffer+3) = save_speed[3];
					buffer[4] = ' ';	
					buffer[5] = '+';
					buffer[6] = '0';
					*(buffer+7)	= rx_buffer[0];
					*(buffer+8)	= rx_buffer[1];
					buffer[9] = '\0';

				}
				else if (   steering_int > 99 )
				{
					*(buffer+0) = save_speed[0];
					*(buffer+1) = save_speed[1];
					*(buffer+2) = save_speed[2];
					*(buffer+3) = save_speed[3];
					buffer[4] = ' ';	
					buffer[5] = '+';
					*(buffer+6)	= rx_buffer[0];
					*(buffer+7)	= rx_buffer[1];
					*(buffer+8)	= rx_buffer[2];
					buffer[9] = '\0';

				}
				else
				{
					*(buffer+0) = save_speed[0];
					*(buffer+1) = save_speed[1];
					*(buffer+2) = save_speed[2];
					*(buffer+3) = save_speed[3];
					buffer[4] = ' ';	
					*(buffer+5)	= rx_buffer[0];
					*(buffer+6)	= rx_buffer[1];
					*(buffer+7)	= rx_buffer[2];
					*(buffer+8)	= rx_buffer[3];
					buffer[9] = '\0';

				}

				*(save_steering+0) = buffer[5];
				*(save_steering+1) = buffer[6];
				*(save_steering+2) = buffer[7];
				*(save_steering+3) = buffer[8];
			 
				affichage(uart0_filestream,buffer,strlen(buffer));
							
				/* Envoie de la commande de propulsion par i2c */
					
				/* Ouverture du bus I2C */
				char *filename = (char*)"/dev/i2c-1";
				if ((file_i2c = open(filename, O_RDWR)) < 0)
				{
						/* Erreur à l'ouverture du bus, vérifier errno pour plus détails */
						printf("Failed to open the i2c bus");
				}
				/* Début de la communication avec l'esclave */
				addr = 0x20;
				if (ioctl(file_i2c, I2C_SLAVE, addr) < 0)
				{
					printf("Failed to acquire bus access and/or talk to slave.\n");
					/* Erreur à la communication avec l'esclave, voir errno pour plus de détails */
				}
				/* Ecriture sur le bus I2C */	
				/* Longueur de la transmission à effectuer - doit correspondre à la tailel du buffer (en octet) */
				length = 9;		
				/* Write retourne le nombre d'octet écris, une erreur apparaît s'il ne correspond pas à la demande envoyée */ 
				if (write(file_i2c, buffer, length) != length)		
				{
					/* Affichage d'une erreur de communication */
					printf("Failed to write to the i2c bus.\n");
				}		
								
			}
			else
			{
				affichage(uart0_filestream,error_speed,strlen(error_speed));
			}	
		}

		else if ( !strcmp(rx_buffer,meas_test) )
		{
			affichage(uart0_filestream,ask_meas,strlen(ask_meas));


			//----- OPEN THE I2C BUS -----
			char *filename = (char*)"/dev/i2c-1";
			if ((file_i2c = open(filename, O_RDWR)) < 0)
			{
				//ERROR HANDLING: you can check errno to see what went wrong
				printf("Failed to open the i2c bus");
			}
	
			addr = 0x30;          //<<<<<The I2C address of the slave
			if (ioctl(file_i2c, I2C_SLAVE, addr) < 0)
			{
				printf("Failed to acquire bus access and/or talk to slave.\n");
				//ERROR HANDLING; you can check errno to see what went wrong
			}

			
			//----- READ BYTES -----
			length = 30;	//<<< Number of bytes to read
			if (read(file_i2c, buffer_reception, length) != length)		//read() returns the number of bytes actually read, if it doesn't match 			then an error occurred (e.g. no response from the device)
			{
				//ERROR HANDLING: i2c transaction failed
				printf("Failed to read from the i2c bus.\n");
			}
			else
			{
			printf("Data read: %s\n", buffer_reception);
			}

			affichage(uart0_filestream,buffer_reception,strlen(buffer_reception));

			
		} 
		else 
		{
			 affichage(uart0_filestream,error_command,strlen(error_command));
		}	
	}	

close(uart0_filestream); 
return 1;
}

