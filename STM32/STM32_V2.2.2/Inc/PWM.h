/*
 * PWM.h
 *
 */

#ifndef PWM_H_
#define PWM_H_

#include "stm32f4xx_hal.h"
#include "cmsis_os.h"

/* PWM_Prop: void function,  */
void PWM_Prop(TIM_HandleTypeDef *htim5, uint8_t Power);

void PWM_Dir(TIM_HandleTypeDef *htim2, uint8_t Power);

#endif /* PWM_H_ */
