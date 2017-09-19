/*
 * PWM.c *
 *
 * */

#include "PWM.h"

void PWM_Dir(TIM_HandleTypeDef *htim2, uint8_t Power) {

	TIM_MasterConfigTypeDef sMasterConfig;
	TIM_OC_InitTypeDef sConfigOC;

	htim2->Instance = TIM2;
	htim2->Init.Prescaler = 0;
	htim2->Init.CounterMode = TIM_COUNTERMODE_UP;
	htim2->Init.Period = 16000000/75;
	htim2->Init.ClockDivision = TIM_CLOCKDIVISION_DIV1;
	if (HAL_TIM_PWM_Init(htim2) != HAL_OK) {
		_Error_Handler(__FILE__, __LINE__);
	}

	sMasterConfig.MasterOutputTrigger = TIM_TRGO_RESET;
	sMasterConfig.MasterSlaveMode = TIM_MASTERSLAVEMODE_DISABLE;

	if (HAL_TIMEx_MasterConfigSynchronization(htim2, &sMasterConfig)
			!= HAL_OK) {
		_Error_Handler(__FILE__, __LINE__);
	}

	sConfigOC.OCMode = TIM_OCMODE_PWM1;

	sConfigOC.Pulse = Power * htim2->Init.Period / 100;

	sConfigOC.OCPolarity = TIM_OCPOLARITY_HIGH;
	sConfigOC.OCFastMode = TIM_OCFAST_DISABLE;

	if (HAL_TIM_PWM_ConfigChannel(htim2, &sConfigOC, TIM_CHANNEL_1) != HAL_OK) {
		_Error_Handler(__FILE__, __LINE__);
	}

	HAL_TIM_MspPostInit(htim2);
	HAL_TIM_PWM_Start(htim2, TIM_CHANNEL_1);
}

void PWM_Prop(TIM_HandleTypeDef *htim5, uint8_t Power) {
	TIM_MasterConfigTypeDef sMasterConfig;
	TIM_OC_InitTypeDef sConfigOC;

	htim5->Instance = TIM5;
	htim5->Init.Prescaler = 0;
	htim5->Init.CounterMode = TIM_COUNTERMODE_UP;
	htim5->Init.Period = 1600;
	htim5->Init.ClockDivision = TIM_CLOCKDIVISION_DIV1;
	if (HAL_TIM_PWM_Init(htim5) != HAL_OK) {
		_Error_Handler(__FILE__, __LINE__);
	}

	sMasterConfig.MasterOutputTrigger = TIM_TRGO_RESET;
	sMasterConfig.MasterSlaveMode = TIM_MASTERSLAVEMODE_DISABLE;
	if (HAL_TIMEx_MasterConfigSynchronization(htim5, &sMasterConfig)
			!= HAL_OK) {
		_Error_Handler(__FILE__, __LINE__);
	}

	sConfigOC.OCMode = TIM_OCMODE_PWM1;
	sConfigOC.Pulse = Power * htim5->Init.Period / 100;
	sConfigOC.OCPolarity = TIM_OCPOLARITY_HIGH;
	sConfigOC.OCFastMode = TIM_OCFAST_DISABLE;
	if (HAL_TIM_PWM_ConfigChannel(htim5, &sConfigOC, TIM_CHANNEL_2) != HAL_OK) {
		_Error_Handler(__FILE__, __LINE__);
	}

	HAL_TIM_MspPostInit(htim5);
	HAL_TIM_PWM_Start(htim5, TIM_CHANNEL_2);
}
