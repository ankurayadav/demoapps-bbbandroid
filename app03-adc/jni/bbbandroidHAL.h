/**********************************************************
  Main bbbandroidHAL header file

  Written by Andrew Henderson (hendersa@icculus.org)
  Modified by Ankur Yadav (ankurayadav@gmail.com)

  This code is made available under the BSD license.
**********************************************************/

#include <stdio.h>

#ifndef __BBBANDROIDHAL_H__
#define __BBBANDROIDHAL_H__

#ifdef __cplusplus
extern "C" {
#endif /* __cplusplus */

/* Library init/shutdown */
extern int openBBBAndroidHAL(void);
extern int closeBBBAndroidHAL(void);

/* ADC interfacing functions */
extern uint32_t readADC(unsigned int channel);

#ifdef __cplusplus
}
#endif /* __cplusplus */
#endif /* __BBBANDROIDHAL_H__ */