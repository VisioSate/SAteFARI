################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
C_SRCS += \
../src/main.c \
../src/stm32f4xx_hal_msp.c \
../src/stm32f4xx_it.c \
../src/system_stm32f4xx.c 

OBJS += \
./src/main.o \
./src/stm32f4xx_hal_msp.o \
./src/stm32f4xx_it.o \
./src/system_stm32f4xx.o 

C_DEPS += \
./src/main.d \
./src/stm32f4xx_hal_msp.d \
./src/stm32f4xx_it.d \
./src/system_stm32f4xx.d 


# Each subdirectory must supply rules for building sources it contributes
src/%.o: ../src/%.c
	@echo 'Building file: $<'
	@echo 'Invoking: MCU GCC Compiler'
	@echo $(PWD)
	arm-none-eabi-gcc -mcpu=cortex-m4 -mthumb -mfloat-abi=hard -mfpu=fpv4-sp-d16 '-D__weak=__attribute__((weak))' '-D__packed=__attribute__((__packed__))' -DUSE_HAL_DRIVER -DSTM32F411xE -I../Inc -I"C:/Users/Maxime/Documents/Projet Intensif/STM32/ADC_Retour_PWM/Drivers/STM32F4xx_HAL_Driver/Inc" -I"C:/Users/Maxime/Documents/Projet Intensif/STM32/ADC_Retour_PWM/Drivers/STM32F4xx_HAL_Driver/Inc/Legacy" -I"C:/Users/Maxime/Documents/Projet Intensif/STM32/ADC_Retour_PWM/Drivers/CMSIS/Device/ST/STM32F4xx/Include" -I"C:/Users/Maxime/Documents/Projet Intensif/STM32/ADC_Retour_PWM/Drivers/CMSIS/Include" -I../Inc  -Og -g3 -Wall -fmessage-length=0 -ffunction-sections -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$@" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


