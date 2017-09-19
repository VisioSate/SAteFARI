/**
  ******************************************************************************
  * File Name          : main.c
  * Description        : Main program body
  ******************************************************************************
  ** This notice applies to any and all portions of this file
  * that are not between comment pairs USER CODE BEGIN and
  * USER CODE END. Other portions of this file, whether 
  * inserted by the user or by software development tools
  * are owned by their respective copyright owners.
  *
  * COPYRIGHT(c) 2017 STMicroelectronics
  *
  * Redistribution and use in source and binary forms, with or without modification,
  * are permitted provided that the following conditions are met:
  *   1. Redistributions of source code must retain the above copyright notice,
  *      this list of conditions and the following disclaimer.
  *   2. Redistributions in binary form must reproduce the above copyright notice,
  *      this list of conditions and the following disclaimer in the documentation
  *      and/or other materials provided with the distribution.
  *   3. Neither the name of STMicroelectronics nor the names of its contributors
  *      may be used to endorse or promote products derived from this software
  *      without specific prior written permission.
  *
  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
  * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
  * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
  * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
  * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
  * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
  * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
  * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
  * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
  * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
  *
  ******************************************************************************
  */
/* Includes ------------------------------------------------------------------*/
#include "main.h"
#include "stm32f4xx_hal.h"

/* Private variables ---------------------------------------------------------*/
TIM_HandleTypeDef htim2;

UART_HandleTypeDef huart2;

/* Private variables ---------------------------------------------------------*/
#define BUFFER_SIZE 100

char TX_buffer[BUFFER_SIZE]= "Visio SATE Corp \n \r";
char RX_buffer[BUFFER_SIZE];

/* Private function prototypes -----------------------------------------------*/
void SystemClock_Config(void);
static void MX_GPIO_Init(void);
static void MX_USART2_UART_Init(void);
static void MX_TIM2_Init(void);                                    
void HAL_TIM_MspPostInit(TIM_HandleTypeDef *htim);
                                
/* Private function prototypes -----------------------------------------------*/

void CLEAR_BUFFER(char *buffer);
void UART_GETS(UART_HandleTypeDef*);
void UART_PUTS(UART_HandleTypeDef*, char*);

void FUNC_SPEED(int spe);
void FUNC_STEERING(int steer);
void FUNC_MEAS();

int main(void)
{
  /* MCU Configuration----------------------------------------------------------*/

  /* Reset of all peripherals, Initializes the Flash interface and the Systick. */
  HAL_Init();

  /* Configure the system clock */
  SystemClock_Config();

  /* Initialize all configured peripherals */
  MX_GPIO_Init();
  MX_USART2_UART_Init();
  MX_TIM2_Init();

  UART_PUTS(&huart2,"\n \r");
  UART_PUTS(&huart2,"*******  *****   ****  *******  ****\n \r");
  UART_PUTS(&huart2,"*  ****  *    *  *  *  *  ****  ****\n \r");
  UART_PUTS(&huart2,"*  *     *  *  * *  *  *  **    ****\n \r");
  UART_PUTS(&huart2,"*  ****  *  **  **  *  *  ****  *  *\n \r");
  UART_PUTS(&huart2,"*  ****  *  * *     *  ****  *  *  *\n \r");
  UART_PUTS(&huart2,"*  *     *  *  *    *    **  *  *  *\n \r");
  UART_PUTS(&huart2,"*  ****  *  *   **  *  ****  *  *  *\n \r");
  UART_PUTS(&huart2,"*  ****  ****    ****  *******  ****\n \r");
  UART_PUTS(&huart2,"SATEFARI - Control Processor Interface\n \r");
  UART_PUTS(&huart2,"enter help to know the supported commands");
  int manual_mode=0;

  /* Infinite loop */
  while (1)
  {
	  UART_Menu(&manual_mode);
  }

}

/** System Clock Configuration
*/
void SystemClock_Config(void)
{

  RCC_OscInitTypeDef RCC_OscInitStruct;
  RCC_ClkInitTypeDef RCC_ClkInitStruct;

    /**Configure the main internal regulator output voltage 
    */
  __HAL_RCC_PWR_CLK_ENABLE();

  __HAL_PWR_VOLTAGESCALING_CONFIG(PWR_REGULATOR_VOLTAGE_SCALE1);

    /**Initializes the CPU, AHB and APB busses clocks 
    */
  RCC_OscInitStruct.OscillatorType = RCC_OSCILLATORTYPE_HSI;
  RCC_OscInitStruct.HSIState = RCC_HSI_ON;
  RCC_OscInitStruct.HSICalibrationValue = 16;
  RCC_OscInitStruct.PLL.PLLState = RCC_PLL_NONE;
  if (HAL_RCC_OscConfig(&RCC_OscInitStruct) != HAL_OK)
  {
    _Error_Handler(__FILE__, __LINE__);
  }

    /**Initializes the CPU, AHB and APB busses clocks 
    */
  RCC_ClkInitStruct.ClockType = RCC_CLOCKTYPE_HCLK|RCC_CLOCKTYPE_SYSCLK
                              |RCC_CLOCKTYPE_PCLK1|RCC_CLOCKTYPE_PCLK2;
  RCC_ClkInitStruct.SYSCLKSource = RCC_SYSCLKSOURCE_HSI;
  RCC_ClkInitStruct.AHBCLKDivider = RCC_SYSCLK_DIV1;
  RCC_ClkInitStruct.APB1CLKDivider = RCC_HCLK_DIV1;
  RCC_ClkInitStruct.APB2CLKDivider = RCC_HCLK_DIV1;

  if (HAL_RCC_ClockConfig(&RCC_ClkInitStruct, FLASH_LATENCY_0) != HAL_OK)
  {
    _Error_Handler(__FILE__, __LINE__);
  }

    /**Configure the Systick interrupt time 
    */
  HAL_SYSTICK_Config(HAL_RCC_GetHCLKFreq()/1000);

    /**Configure the Systick 
    */
  HAL_SYSTICK_CLKSourceConfig(SYSTICK_CLKSOURCE_HCLK);

  /* SysTick_IRQn interrupt configuration */
  HAL_NVIC_SetPriority(SysTick_IRQn, 0, 0);
}

/* TIM2 init function */
static void MX_TIM2_Init(void)
{

  TIM_MasterConfigTypeDef sMasterConfig;
  TIM_OC_InitTypeDef sConfigOC;

  htim2.Instance = TIM2;
  htim2.Init.Prescaler = 0;
  htim2.Init.CounterMode = TIM_COUNTERMODE_UP;
  htim2.Init.Period = 0;
  htim2.Init.ClockDivision = TIM_CLOCKDIVISION_DIV1;
  if (HAL_TIM_PWM_Init(&htim2) != HAL_OK)
  {
    _Error_Handler(__FILE__, __LINE__);
  }

  sMasterConfig.MasterOutputTrigger = TIM_TRGO_RESET;
  sMasterConfig.MasterSlaveMode = TIM_MASTERSLAVEMODE_DISABLE;
  if (HAL_TIMEx_MasterConfigSynchronization(&htim2, &sMasterConfig) != HAL_OK)
  {
    _Error_Handler(__FILE__, __LINE__);
  }

  sConfigOC.OCMode = TIM_OCMODE_PWM1;
  sConfigOC.Pulse = 0;
  sConfigOC.OCPolarity = TIM_OCPOLARITY_HIGH;
  sConfigOC.OCFastMode = TIM_OCFAST_DISABLE;
  if (HAL_TIM_PWM_ConfigChannel(&htim2, &sConfigOC, TIM_CHANNEL_1) != HAL_OK)
  {
    _Error_Handler(__FILE__, __LINE__);
  }

  if (HAL_TIM_PWM_ConfigChannel(&htim2, &sConfigOC, TIM_CHANNEL_2) != HAL_OK)
  {
    _Error_Handler(__FILE__, __LINE__);
  }

  HAL_TIM_MspPostInit(&htim2);

}

/* USART2 init function */
static void MX_USART2_UART_Init(void)
{

  huart2.Instance = USART2;
  huart2.Init.BaudRate = 115200;
  huart2.Init.WordLength = UART_WORDLENGTH_8B;
  huart2.Init.StopBits = UART_STOPBITS_1;
  huart2.Init.Parity = UART_PARITY_NONE;
  huart2.Init.Mode = UART_MODE_TX_RX;
  huart2.Init.HwFlowCtl = UART_HWCONTROL_NONE;
  huart2.Init.OverSampling = UART_OVERSAMPLING_16;
  if (HAL_UART_Init(&huart2) != HAL_OK)
  {
    _Error_Handler(__FILE__, __LINE__);
  }

}

/** Pinout Configuration
*/
static void MX_GPIO_Init(void)
{

  /* GPIO Ports Clock Enable */
  __HAL_RCC_GPIOA_CLK_ENABLE();

}

void PWM_Motor(uint32_t Channel,uint8_t Power){
	  TIM_MasterConfigTypeDef sMasterConfig;
	  TIM_OC_InitTypeDef sConfigOC;

	  htim2.Instance = TIM2;
	  htim2.Init.Prescaler = 0;
	  htim2.Init.CounterMode = TIM_COUNTERMODE_UP;
	  htim2.Init.Period = 800;
	  htim2.Init.ClockDivision = TIM_CLOCKDIVISION_DIV1;
	  if (HAL_TIM_PWM_Init(&htim2) != HAL_OK)
	  {
	    _Error_Handler(__FILE__, __LINE__);
	  }

	  sMasterConfig.MasterOutputTrigger = TIM_TRGO_RESET;
	  sMasterConfig.MasterSlaveMode = TIM_MASTERSLAVEMODE_DISABLE;
	  if (HAL_TIMEx_MasterConfigSynchronization(&htim2, &sMasterConfig) != HAL_OK)
	  {
	    _Error_Handler(__FILE__, __LINE__);
	  }

	  sConfigOC.OCMode = TIM_OCMODE_PWM1;
	  sConfigOC.Pulse = Power*htim2.Init.Period/100;
	  sConfigOC.OCPolarity = TIM_OCPOLARITY_HIGH;
	  sConfigOC.OCFastMode = TIM_OCFAST_DISABLE;
	  if (HAL_TIM_PWM_ConfigChannel(&htim2, &sConfigOC, Channel) != HAL_OK)
	  {
	    _Error_Handler(__FILE__, __LINE__);
	  }

	  HAL_TIM_MspPostInit(&htim2);
	  HAL_TIM_PWM_Start(&htim2,Channel);
}

void PWM_Servo(uint32_t Channel, int Power, uint32_t period){
	  TIM_MasterConfigTypeDef sMasterConfig;
	  TIM_OC_InitTypeDef sConfigOC;

	  htim2.Instance = TIM2;
	  htim2.Init.Prescaler = 0;
	  htim2.Init.CounterMode = TIM_COUNTERMODE_UP;
	  htim2.Init.Period = period;
	  htim2.Init.ClockDivision = TIM_CLOCKDIVISION_DIV1;
	  if (HAL_TIM_PWM_Init(&htim2) != HAL_OK)
	  {
	    _Error_Handler(__FILE__, __LINE__);
	  }

	  sMasterConfig.MasterOutputTrigger = TIM_TRGO_RESET;
	  sMasterConfig.MasterSlaveMode = TIM_MASTERSLAVEMODE_DISABLE;
	  if (HAL_TIMEx_MasterConfigSynchronization(&htim2, &sMasterConfig) != HAL_OK)
	  {
	    _Error_Handler(__FILE__, __LINE__);
	  }

	  sConfigOC.OCMode = TIM_OCMODE_PWM1;
	  sConfigOC.Pulse = Power*htim2.Init.Period/100;
	  sConfigOC.OCPolarity = TIM_OCPOLARITY_HIGH;
	  sConfigOC.OCFastMode = TIM_OCFAST_DISABLE;
	  if (HAL_TIM_PWM_ConfigChannel(&htim2, &sConfigOC, Channel) != HAL_OK)
	  {
	    _Error_Handler(__FILE__, __LINE__);
	  }

	  HAL_TIM_MspPostInit(&htim2);
	  HAL_TIM_PWM_Start(&htim2,Channel);
}

void UART_Menu(int* manual_mode){
	int tmp_value = 0;
	 UART_PUTS(&huart2, "\n \r");
		  UART_PUTS(&huart2, "control:~# ");
		  UART_GETS(& huart2);

		  if (!strcmp(RX_buffer, "\r"));

		  /* test if command is help */
		  else if (!strcmp(RX_buffer, "help\r")){

			  			UART_PUTS(&huart2, "\n \r");

			  			UART_PUTS(&huart2, "auto\t\t: start automatic mode and stop manual mode\n \r");
			  			UART_PUTS(&huart2, "manual\t\t: start manual mode and stop automatic mode\n \r");
			  			UART_PUTS(&huart2, "speed\t\t: update speed value (only in manual mode)\n \r");
			  			UART_PUTS(&huart2, "steering\t: update steering value (only in manual mode)\n \r");
			  			UART_PUTS(&huart2, "meas\t\t: print current - voltage - power in real time (only in manual mode).");

		  }
		  else if (!strcmp(RX_buffer, "auto\r")){

			  	  	  	UART_PUTS(&huart2, "\n \r");

			  	  	  	UART_PUTS(&huart2, "Switching to automatic mode");

			  	  	  	* manual_mode = 0;

		  }
		  else if (!strcmp(RX_buffer, "manual\r")){

			  	  	  	UART_PUTS(&huart2, "\n \r");

			  		  	UART_PUTS(&huart2, "Switching to manual mode");

			  		    *manual_mode = 1;

		  }
		  else if (!strcmp(RX_buffer, "speed\r")){

		  		  	  	UART_PUTS(&huart2, "\n \r");

		  		  	  	if (*manual_mode){
		  		  	  		UART_PUTS(&huart2, "enter value between 0 (stop) and 1023 (full speed) :\n \r");

		  		  	  		UART_GETS(&huart2);
		  		  	  		UART_PUTS(&huart2, "\n \r");

		  		  	  		tmp_value = strtol(RX_buffer);
		  		  	  		if (tmp_value < 0 || tmp_value > 1023){
		  		  	  			UART_PUTS(&huart2, "error: failed value");
		  		  	  		}
		  		  	  		else{
		  		  	  			FUNC_SPEED(tmp_value);
		  		  	  		}
		  		  	  	}
		  		  	  	else{
		  		  	  		UART_PUTS(&huart2, "error: automatic mode enabled, switch to manual mode to use this command\n \r");
		  		  	  	}
		  }
		  else if (!strcmp(RX_buffer, "steering\r")){

			  	  	  	  UART_PUTS(&huart2, "\n \r");

			  	  	  	  if (*manual_mode){

			  	  	  		  while(1){

			  	  	  		  UART_PUTS(&huart2, "enter value between -127 (full left) and 128 (full right), 0 is the middle position :\n \r");
			  	  	  		  UART_GETS(&huart2);
			  	  	  		  UART_PUTS(&huart2, "\n \r");

			  	  	  		  tmp_value = strtol(RX_buffer);

			  	  	  		  if (tmp_value < -127 || tmp_value > 128){
			  	  	  			  UART_PUTS(&huart2, "error: failed value");
			  	  	  		  }
			  	  	  		  else{
			  	  	  			  FUNC_STEERING(tmp_value);
			  	  	  		 }

			  	  	  	  }
			  	  	  	  }
			  	  	  	  else{
			  	  	  		  UART_PUTS(&huart2, "error: automatic mode enabled, switch to manual mode to use this command\n \r");
			  	  	  	  }
		  }
		  else if (!strcmp(RX_buffer, "meas\r")){

		  		  	  	  	  UART_PUTS(&huart2, "\n \r");

		  		  	  	  	  if (*manual_mode){
		  		  	  	  		  FUNC_MEAS(tmp_value);
		  		  	  	  	  }
		  		  	  	  	  else{
		  		  	  	  		  UART_PUTS(&huart2, "error: automatic mode enabled, switch to manual mode to use this command\n \r");
		  		  	  	  	  }
		  	  }
		  else{
			  UART_PUTS(&huart2, "\n \r");

			  UART_PUTS(&huart2, "error: command not supported. Enter help to know the supported commands");
		  }

}
void CLEAR_BUFFER(char *buffer){
	int i;
	for(i=0; i<BUFFER_SIZE; i++){
			  buffer[i]=0;
	}
}

void UART_GETS(UART_HandleTypeDef * huart){
	  char c_buffer;
	  uint8_t i = 0;
	  CLEAR_BUFFER(RX_buffer);
	  do{
		  HAL_UART_Receive(huart, &c_buffer, 1, HAL_MAX_DELAY);

		  if (c_buffer == '\b'){
			  if (i==0)
				  continue;
			  RX_buffer[i--] = '\0';
			  HAL_UART_Transmit(&huart2, "\b \b", 3, 1);
		  }
		  else if (!((c_buffer > 47 && c_buffer < 58) || (c_buffer > 96 && c_buffer < 123) || c_buffer == 45) && c_buffer != '\r'){
			  continue;
		  }
		  else{
			  RX_buffer[i++] = c_buffer;
			  HAL_UART_Transmit(&huart2, &c_buffer, 1, 1);
		  }
	  } while((c_buffer != '\r') && (i < BUFFER_SIZE));
}

void UART_PUTS(UART_HandleTypeDef * huart, char* str){
		char *c_ptr = str;

		CLEAR_BUFFER(TX_buffer);
		sprintf(TX_buffer, str);

		while(*c_ptr != '\0' && c_ptr-str<BUFFER_SIZE){
			HAL_UART_Transmit(& huart2,c_ptr,1,1);
			++c_ptr;
		}
}

void FUNC_SPEED(int spe){
	PWM_Motor(TIM_CHANNEL_1,spe*100/1023);
}

void FUNC_STEERING(int steer){

	int f = 75; /* Hz */
	int command = (128-steer)*5/255 + 9;

	PWM_Servo(TIM_CHANNEL_2,command, 16000000/f);
}

void FUNC_MEAS(){
	UART_PUTS(&huart2, "func_meas\n \r");
}

/**
  * @brief  This function is executed in case of error occurrence.
  * @param  None
  * @retval None
  */
void _Error_Handler(char * file, int line)
{
  while(1) 
  {
  }
}

#ifdef USE_FULL_ASSERT

/**
   * @brief Reports the name of the source file and the source line number
   * where the assert_param error has occurred.
   * @param file: pointer to the source file name
   * @param line: assert_param error line source number
   * @retval None
   */
void assert_failed(uint8_t* file, uint32_t line)
{
}

#endif

/**
  * @}
  */ 

/**
  * @}
*/ 

/************************ (C) COPYRIGHT STMicroelectronics *****END OF FILE****/
