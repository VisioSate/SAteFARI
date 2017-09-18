/*
 * UART.c
 *
 *  Created on: 17 sept. 2017
 *      Author: Benoit
*/
#include "UARTmenu.h"

char TX_Buffer[BUFFER_SIZE]= "Visio SATE Corp \n \r";
char RX_Buffer[BUFFER_SIZE];


void UART_START_MENU(UART_HandleTypeDef* huart2){

	UART_PUTS(huart2, "\n \r");
	UART_PUTS(huart2, "*******  *****   ****  *******  ****\n \r");
	UART_PUTS(huart2, "*  ****  *    *  *  *  *  ****  ****\n \r");
	UART_PUTS(huart2, "*  *     *  *  * *  *  *  **    ****\n \r");
	UART_PUTS(huart2, "*  ****  *  **  **  *  *  ****  *  *\n \r");
	UART_PUTS(huart2, "*  ****  *  * *     *  ****  *  *  *\n \r");
	UART_PUTS(huart2, "*  *     *  *  *    *    **  *  *  *\n \r");
	UART_PUTS(huart2, "*  ****  *  *   **  *  ****  *  *  *\n \r");
	UART_PUTS(huart2, "*  ****  ****    ****  *******  ****\n \r");
	UART_PUTS(huart2, "SATEFARI - Control Processor Interface\n \r");
	UART_PUTS(huart2, "enter help to know the supported commands");
}
/* CLEAR_BUFFER */
void CLEAR_BUFFER(char *buffer){
	int i;
	for(i=0; i < BUFFER_SIZE; i++){
			  buffer[i] = 0;
	}
}

/* UART_GETS */
void UART_GETS(UART_HandleTypeDef * huart2){
	  char c_buffer;
	  uint8_t i = 0;
	  CLEAR_BUFFER(RX_Buffer);
	  do{
		  HAL_UART_Receive(huart2, &c_buffer, 1, HAL_MAX_DELAY);

		  if (c_buffer == '\b'){
			  if (i==0)
				  continue;
			  RX_Buffer[i--] = '\0';
			  HAL_UART_Transmit(huart2, "\b \b", 3, 1);
		  }
		  else if (!((c_buffer > 47 && c_buffer < 58) || (c_buffer > 96 && c_buffer < 123) || c_buffer == '-') && c_buffer != '\r'){
			  continue;
		  }
		  else{
			  RX_Buffer[i++] = c_buffer;
			  HAL_UART_Transmit(huart2, &c_buffer, 1, 1);
		  }
	  } while((c_buffer != '\r') && (i < BUFFER_SIZE));
}

/* UART_PUTS */
void UART_PUTS(UART_HandleTypeDef * huart, char* str){
		char *c_ptr = str;
		CLEAR_BUFFER(TX_Buffer);
		sprintf(TX_Buffer, str);

		while(*c_ptr != '\0' && c_ptr-str < BUFFER_SIZE){
			HAL_UART_Transmit(huart, c_ptr, 1, 1);
			++c_ptr;
		}
}

/* FUNC_SPEED */
void FUNC_SPEED(int spe,osMessageQId * Queue_PropulsionHandle_ptr){
	osMessagePut(*Queue_PropulsionHandle_ptr,spe,200);
}

/* FUNC_STEERING */
void FUNC_STEERING(int steer,osMessageQId * queue_DirectionHandle_ptr){
	uint32_t steer_uint = steer +127;
	osMessagePut(*queue_DirectionHandle_ptr,steer_uint,200);
}

/* FUNC_MEAS */
void FUNC_MEAS(){

}

void UART_MENU(UART_HandleTypeDef* huart2,int *manual_mode,osMessageQId * Queue_PropulsionHandle_ptr,osMessageQId * queue_DirectionHandle_ptr){
	/* tmp_value: int used to get numeric value from RX_Buffer for speed and steering functions */
	int tmp_value = 0;
	/* UART MENU: based on given example */
	UART_PUTS(huart2, "\n \r");
	UART_PUTS(huart2, "control:~# ");
	UART_GETS(huart2);

	/* test to ignore empty command, does not prompt command error message */
	if (!strcmp(RX_Buffer, "\r")){/*continue*/}

	/* test if command is help */
	else if (!strcmp(RX_Buffer, "help\r")){

		UART_PUTS(huart2, "\n \r");

		UART_PUTS(huart2, "auto\t\t: start automatic mode and stop manual mode\n \r");
		UART_PUTS(huart2, "manual\t\t: start manual mode and stop automatic mode\n \r");
		UART_PUTS(huart2, "speed\t\t: update speed value (only in manual mode)\n \r");
		UART_PUTS(huart2, "steering\t: update steering value (only in manual mode)\n \r");
		UART_PUTS(huart2, "meas\t\t: print current - voltage - power in real time (only in manual mode).");

	}
	/* test if command is auto */
	else if (!strcmp(RX_Buffer, "auto\r")){
		if (!*manual_mode){
			/* do nothing */
		}
		else{
			UART_PUTS(huart2, "\n \r");

			UART_PUTS(huart2, "Switching to automatic mode");

			*manual_mode = 0;
		}
	}
	/* test if command is manual */
	else if (!strcmp(RX_Buffer, "manual\r")){
		if (*manual_mode){
			/* do nothing */
		}
		else{
			UART_PUTS(huart2, "\n \r");

			UART_PUTS(huart2, "Switching to manual mode");

			*manual_mode = 1;
		}
	}
	/* test if command is speed */
	else if (!strcmp(RX_Buffer, "speed\r")){

		UART_PUTS(huart2, "\n \r");

		if (*manual_mode){
			UART_PUTS(huart2, "enter value between 0 (stop) and 1023 (full speed) :\n \r");

			UART_GETS(huart2);
			UART_PUTS(huart2, "\n \r");

			tmp_value = strtol(RX_Buffer);
			/* test if value is in range [0:1023] */
			if (tmp_value < 0 || tmp_value > 1023){
				UART_PUTS(huart2, "error: failed value");
			}
			else{
				/* call FUNC_SPEED */
				FUNC_SPEED(tmp_value,Queue_PropulsionHandle_ptr);
			}
		}
		else{
			UART_PUTS(huart2, "error: automatic mode enabled, switch to manual mode to use this command\n \r");
		}
	}
	/* test if command is steering */
	else if (!strcmp(RX_Buffer, "steering\r")){

		UART_PUTS(huart2, "\n \r");

			if (*manual_mode){
				UART_PUTS(huart2, "enter value between -127 (full left) and 128 (full right), 0 is the middle position :\n \r");

				UART_GETS(huart2);
				UART_PUTS(huart2, "\n \r");

				tmp_value = strtol(RX_Buffer);
				/* test if value in range [-127:128] */
			  	if (tmp_value < -127 || tmp_value > 128){
			  		UART_PUTS(huart2, "error: failed value");
			  	}
			  	else{
			  		/* call FUNC_STEERING */
			  		FUNC_STEERING(tmp_value,queue_DirectionHandle_ptr);
			  	}
			}
			else{
				UART_PUTS(huart2, "error: automatic mode enabled, switch to manual mode to use this command\n \r");
			}
	}
		/* test if command is meas */
	else if (!strcmp(RX_Buffer, "meas\r")){

		UART_PUTS(huart2, "\n \r");

		if (*manual_mode){
			FUNC_MEAS(tmp_value);
		}
		else{
			UART_PUTS(huart2, "error: automatic mode enabled, switch to manual mode to use this command\n \r");
		}
	}
	/* Unknown command */
	else{
		UART_PUTS(huart2, "\n \r");

		UART_PUTS(huart2, "error: command not supported. Enter help to know the supported commands");
	}
}
