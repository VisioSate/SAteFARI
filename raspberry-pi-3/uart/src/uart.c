#include "uart.h"

int main(void)
{
	/* SETUP USART 0
	At bootup, pins 8 and 10 are already set to UART0_TXD, UART0_RXD (ie the alt0 function) respectively */
	int uart0_filestream = -1;

	/*OPEN THE UART
	The flags (defined in fcntl.h):
		Access modes (use 1 of these):
			O_RDONLY - Open for reading only.
			O_RDWR - Open for reading and writing.
			O_WRONLY - Open for writing only.

		O_NDELAY / O_NONBLOCK (same function) - Enables nonblocking mode. When set read requests on the file can return immediately with a failure status
												if there is no input immediately available (instead of blocking). Likewise, write requests can also return
												immediately with a failure status if the output can't be written immediately.

		O_NOCTTY - When set and path identifies a terminal device, open() shall not cause the terminal device to become the controlling terminal for the process.*/
	uart0_filestream = open("/dev/ttyAMA0", O_RDWR);

	if (uart0_filestream == -1)
	{
		/* ERROR - CAN'T OPEN SERIAL PORT */
		printf("Error - Unable to open UART.  Ensure it is not in use by another application\n");
	}

	/* CONFIGURE THE UART
		//The flags (defined in /usr/include/termios.h - see http://pubs.opengroup.org/onlinepubs/007908799/xsh/termios.h.html):
		Baud rate:- B1200, B2400, B4800, B9600, B19200, B38400, B57600, B115200, B230400, B460800, B500000, B576000, B921600, B1000000, B1152000, B1500000, B2000000, B2500000, B3000000, B3500000, B4000000
		CSIZE:- CS5, CS6, CS7, CS8
		CLOCAL - Ignore modem status lines
		CREAD - Enable receiver
		IGNPAR = Ignore characters with parity errors
		ICRNL - Map CR to NL on input (Use for ASCII comms where you want to auto correct end of line characters - don't use for bianry comms!)
		PARENB - Parity enable
		PARODD - Odd parity (else even) */
	struct termios options;
	tcgetattr(uart0_filestream, &options);
	options.c_cflag = B9600 | CS8 | CLOCAL | CREAD;		//<Set baud rate
	options.c_iflag = IGNPAR;
	options.c_oflag = 0;
	options.c_lflag = 0;
	tcflush(uart0_filestream, TCIFLUSH);
	tcsetattr(uart0_filestream, TCSANOW, &options);

   	/* Affichage du message d'accueil */
	unsigned char* acceuil =
"\r\n*******  *****   ****  *******  ****\r\n*  ****  *    *  *  *  *  ****  ****\r\n*  *     *  *  * *  *  *  *     ****\r\n*  ***   *  **  **  *  *  ****  *  *\r\n*  ***   *  * *  *  *  ****  *  *  *\r\n*  *     *  *  *    *     *  *  *  *\r\n*  ****	 *  *   *   *  ****  *  *  *\r\n*******  ****    ****  *******  ****\r\nSATEFARI - Supervison Processor Interface\r\nEnter help to know the supported commands";


	unsigned char* help = "speed     : update speed value\r\nsteering  : update steering value\r\nmeas      : print current - voltage power in real time.\r\nPress enter to quit";
	unsigned char* askspeed = "Enter value between 0 (stop) and 1023 (full speed) :";
	unsigned char* asksteering = "Enter value between -127 (full left) and 128 (full right), 0 is the middle position :";
	unsigned char* askmeas = "currents and voltage analog values for propulsion module : ";
	unsigned char* sucspeed = "change speed succeeded";
	unsigned char* sucsteering = "change steering succeeded";
	unsigned char* errspeed = "error : failed value";
	unsigned char* errcommand = "Error : command not supported. Enter help to know the supported commands";

	/* Adresse de l'esclave pour la prochaine communication I2C */
	int addr = 0x20;
	int length = 9;
	int file_i2c;


	unsigned char* commandline = "\r\ncontrol:~# ";
	unsigned char* carreturn = "\r\n";
	unsigned char* i2cbuf[256];
	unsigned char  rxbuf[256];
	unsigned char  txbuf[9];
	unsigned char  savespeed[250]= "0000";
	unsigned char  savesteering[250]="+000";
    unsigned char* cmphelp = "help\r";
	unsigned char* cmpspeed = "speed\r";
	unsigned char* cmpsteering = "steering\r";
	unsigned char* cmpmeas = "meas\r";

	int intspeed;
	int intsteering;

	char *endptr;

	affichage(uart0_filestream,acceuil,strlen(acceuil));

	while (1)
    {

		affichage(uart0_filestream,commandline,strlen(commandline));
		receive(uart0_filestream,rxbuf);
		affichage(uart0_filestream,carreturn,strlen(carreturn));

		if ( !strcmp(rxbuf,cmphelp))
		{
			affichage(uart0_filestream,help,strlen(help));
			receive(uart0_filestream,rxbuf);
		}

		else if ( !strcmp(rxbuf,cmpspeed) )
		{
			affichage(uart0_filestream,askspeed,strlen(askspeed));
			affichage(uart0_filestream,carreturn,strlen(carreturn));
			receive(uart0_filestream,rxbuf);
			affichage(uart0_filestream,carreturn,strlen(carreturn));

			intspeed = strtol( rxbuf, &endptr, 10);

			if (endptr == rxbuf)
			{
				affichage(uart0_filestream,errspeed,strlen(errspeed));
			}
			else if ( (intspeed <= 1023) && (intspeed >=0) )
			{

				affichage(uart0_filestream,sucspeed,strlen(sucspeed));
				affichage(uart0_filestream,carreturn,strlen(carreturn));

				if (intspeed < 10)
				{

					txbuf[0] = '0';
					txbuf[1] = '0';
					txbuf[2] = '0';
					*(txbuf+3) = rxbuf[0];
					txbuf[4] = ' ';
					*(txbuf+5) = savesteering[0];
					*(txbuf+6) = savesteering[1];
					*(txbuf+7) = savesteering[2];
					*(txbuf+8) = savesteering[3];
					txbuf[9] = '\0';
				}

				else if (intspeed < 100)
				{

					txbuf[0] = '0';
					txbuf[1] = '0';
					*(txbuf+2) = rxbuf[0];
					*(txbuf+3) = rxbuf[1];
					txbuf[4] = ' ';
					*(txbuf+5) = savesteering[0];
					*(txbuf+6) = savesteering[1];
					*(txbuf+7) = savesteering[2];
					*(txbuf+8) = savesteering[3];
					txbuf[9] = '\0';
				}

				else if (intspeed < 1000)
				{

					txbuf[0] = '0';
					*(txbuf+3) = rxbuf[2];
					*(txbuf+2) = rxbuf[1];
					*(txbuf+1) = rxbuf[0];
					txbuf[4] = ' ';
					*(txbuf+5) = savesteering[0];
					*(txbuf+6) = savesteering[1];
					*(txbuf+7) = savesteering[2];
					*(txbuf+8) = savesteering[3];
					txbuf[9] = '\0';
				}

				else
				{
					*(txbuf+3) = rxbuf[3];
					*(txbuf+2) = rxbuf[2];
					*(txbuf+1) = rxbuf[1];
					*(txbuf) = rxbuf[0];
					txbuf[4] = ' ';
					*(txbuf+5) = savesteering[0];
					*(txbuf+6) = savesteering[1];
					*(txbuf+7) = savesteering[2];
					*(txbuf+8) = savesteering[3];
					txbuf[9] = '\0';
				}

				*(savespeed+0) = txbuf[0];
				*(savespeed+1) = txbuf[1];
				*(savespeed+2) = txbuf[2];
				*(savespeed+3) = txbuf[3];
				affichage(uart0_filestream,txbuf,strlen(txbuf));


				/* Envoie de la commande de propulsion par i2c */

				/* Ouverture du bus I2C */
				char *filename = (char*)"/dev/i2c-1";
				if ((file_i2c = open(filename, O_RDWR)) < 0)
				{
						/* Erreur à l'ouverture du bus, vérifier errno pour plus détails */
						printf("Failed to open the i2c bus");
				}
				/* Début de la communication avec l'esclave */
				if (ioctl(file_i2c, I2C_SLAVE, addr) < 0)
				{
					printf("Failed to acquire bus access and/or talk to slave.\n");
					/* Erreur à la communication avec l'esclave, voir errno pour plus de détails */
				}
				/* Ecriture sur le bus I2C */
				/* Longueur de la transmission à effectuer - doit correspondre à la taille du buffer (en octet) */
				length = 9;
				/* Write retourne le nombre d'octet écris, une erreur apparaît s'il ne correspond pas à la demande envoyée */
				if (write(file_i2c, txbuf, length) != length)
				{
					/* Affichage d'une erreur de communication */
					printf("Failed to write to the i2c bus.\n");
				}

			}

			else
			{
				affichage(uart0_filestream,errspeed,strlen(errspeed));
			}
		}

		else if ( !strcmp(rxbuf,cmpsteering) )
		{

			affichage(uart0_filestream,asksteering,strlen(asksteering));
			affichage(uart0_filestream,carreturn,strlen(carreturn));
			receive(uart0_filestream,rxbuf);
			affichage(uart0_filestream,carreturn,strlen(carreturn));

			intsteering = strtol( rxbuf, &endptr, 10);

			if (endptr == rxbuf)
			{
				affichage(uart0_filestream,errspeed,strlen(errspeed));
			}
			else if ( ( intsteering <= 128 ) && (intsteering >= -127) )
			{
				affichage(uart0_filestream,sucsteering,strlen(sucsteering));
				affichage(uart0_filestream,carreturn,strlen(carreturn));

    			if ( (intsteering < -9) && (intsteering > -100) )
				{

					*(txbuf+0) = savespeed[0];
					*(txbuf+1) = savespeed[1];
					*(txbuf+2) = savespeed[2];
					*(txbuf+3) = savespeed[3];
					txbuf[4] = ' ';
					*(txbuf+5)	= rxbuf[0];
					txbuf[6] = '0';
					*(txbuf+7) = rxbuf[1];
					*(txbuf+8)	= rxbuf[2];
					txbuf[9] = '\0';
				}

				else if (   (intsteering > -10) && (intsteering < 0 ) )
				{

					*(txbuf+0) = savespeed[0];
					*(txbuf+1) = savespeed[1];
					*(txbuf+2) = savespeed[2];
					*(txbuf+3) = savespeed[3];
					txbuf[4] = ' ';
					*(txbuf+5)	= rxbuf[0];
					txbuf[6] = '0';
					txbuf[7] = '0';
					*(txbuf+8)	= rxbuf[1];
					txbuf[9] = '\0';

				}

				else if (   (intsteering > -1) && (intsteering < 10 ) )
				{

					*(txbuf+0) = savespeed[0];
					*(txbuf+1) = savespeed[1];
					*(txbuf+2) = savespeed[2];
					*(txbuf+3) = savespeed[3];
					txbuf[4] = ' ';
					txbuf[5] = '+';
					txbuf[6] = '0';
					txbuf[7] = '0';
					*(txbuf+8)	= rxbuf[0];
					txbuf[9] = '\0';

				}
				else if (   (intsteering > 9) && (intsteering < 100 ) )
				{
					*(txbuf+0) = savespeed[0];
					*(txbuf+1) = savespeed[1];
					*(txbuf+2) = savespeed[2];
					*(txbuf+3) = savespeed[3];
					txbuf[4] = ' ';
					txbuf[5] = '+';
					txbuf[6] = '0';
					*(txbuf+7)	= rxbuf[0];
					*(txbuf+8)	= rxbuf[1];
					txbuf[9] = '\0';

				}
				else if (   intsteering > 99 )
				{
					*(txbuf+0) = savespeed[0];
					*(txbuf+1) = savespeed[1];
					*(txbuf+2) = savespeed[2];
					*(txbuf+3) = savespeed[3];
					txbuf[4] = ' ';
					txbuf[5] = '+';
					*(txbuf+6)	= rxbuf[0];
					*(txbuf+7)	= rxbuf[1];
					*(txbuf+8)	= rxbuf[2];
					txbuf[9] = '\0';

				}
				else
				{
					*(txbuf+0) = savespeed[0];
					*(txbuf+1) = savespeed[1];
					*(txbuf+2) = savespeed[2];
					*(txbuf+3) = savespeed[3];
					txbuf[4] = ' ';
					*(txbuf+5)	= rxbuf[0];
					*(txbuf+6)	= rxbuf[1];
					*(txbuf+7)	= rxbuf[2];
					*(txbuf+8)	= rxbuf[3];
					txbuf[9] = '\0';

				}

				*(savesteering+0) = txbuf[5];
				*(savesteering+1) = txbuf[6];
				*(savesteering+2) = txbuf[7];
				*(savesteering+3) = txbuf[8];

				affichage(uart0_filestream,txbuf,strlen(txbuf));

				/* Envoie de la commande de propulsion par i2c */

				/* Ouverture du bus I2C */
				char *filename = (char*)"/dev/i2c-1";
				if ((file_i2c = open(filename, O_RDWR)) < 0)
				{
						/* Erreur à l'ouverture du bus, vérifier errno pour plus détails */
						printf("Failed to open the i2c bus");
				}
				/* Début de la communication avec l'esclave */
				if (ioctl(file_i2c, I2C_SLAVE, addr) < 0)
				{
					printf("Failed to acquire bus access and/or talk to slave.\n");
					/* Erreur à la communication avec l'esclave, voir errno pour plus de détails */
				}
				/* Ecriture sur le bus I2C */
				/* Longueur de la transmission à effectuer - doit correspondre à la tailel du buffer (en octet) */
				length = 9;
				/* Write retourne le nombre d'octet écris, une erreur apparaît s'il ne correspond pas à la demande envoyée */
				if (write(file_i2c, txbuf, length) != length)
				{
					/* Affichage d'une erreur de communication */
					printf("Failed to write to the i2c bus.\n");
				}

			}
			else
			{
				affichage(uart0_filestream,errspeed,strlen(errspeed));
			}
		}

		else if ( !strcmp(rxbuf,cmpmeas) )
		{
			affichage(uart0_filestream,askmeas,strlen(askmeas));


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
			if (read(file_i2c, i2cbuf, length) != length)		//read() returns the number of bytes actually read, if it doesn't match 			then an error occurred (e.g. no response from the device)
			{
				//ERROR HANDLING: i2c transaction failed
				printf("Failed to read from the i2c bus.\n");
			}
			else
			{
			printf("Data read: %s\n", i2cbuf);
			}

			affichage(uart0_filestream,i2cbuf,strlen(i2cbuf));



		}
		else
		{
			 affichage(uart0_filestream,errcommand,strlen(errcommand));
		}
	}

close(uart0_filestream);
return 1;
}
