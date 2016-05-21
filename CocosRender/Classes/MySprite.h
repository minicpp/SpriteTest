#ifndef __MY_SPRITE__
#define __MY_SPRITE__

#include "cocos2d.h"
USING_NS_CC;

class MySprite 
{
public:
	Sprite *sp;
	double speedX;
	double speedY;
	double posX;
	double posY;
	double halfWidth;
	double halfHeight;
	double width;
	double height;
	void setSize(float w, float h);
	MySprite(Texture2D *p, double w, double h);
	virtual ~MySprite();
};

#endif