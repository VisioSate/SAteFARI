///*
// * UART.h
// *
// *  Created on: 17 sept. 2017
// *      Author: Benoit
// */
//
#ifndef UARTMENU_H_
#define UARTMENU_H_

#include "stm32f4xx_hal.h"
#include "cmsis_os.h"

#define BUFFER_SIZE				100

/* CLEAR_BUFFER: void function, set every char in RX_buffer or TX_buffer to 'NUL' */
void CLEAR_BUFFER(char *buffer);

/* UART_GETS: void function, get strings from serial communication in RX_buffer, in the BUFFER_SIZE limit of char */
void UART_GETS(UART_HandleTypeDef*);

/* UART_PUTS: void function, send given string to serial communication, using TX_buffer */
void UART_PUTS(UART_HandleTypeDef*, char*);

/* FUNC_SPEED: void function, execute the speed command */
void FUNC_SPEED(int spe,osMessageQId * Queue_PropulsionHandle_ptr);

/* FUNC_STEERING: void function, execute the steering command */
void FUNC_STEERING(int steer,osMessageQId * queue_DirectionHandle_ptr);

/* FUNC_MEAS: void function, execute the meas command */
void FUNC_MEAS(UART_HandleTypeDef * huart,osMessageQId * Queue_ISENSEHandle_ptr);

void UART_MENU(UART_HandleTypeDef* huart2,int *manual_mode,int *etat_LED, osMessageQId * Queue_PropulsionHandle_ptr,osMessageQId * queue_DirectionHandle_ptr,osMessageQId * queue_ISENSEHandle_ptr);

void UART_START_MENU(UART_HandleTypeDef* huart2);

#endif /* UARTMENU_H_ */
