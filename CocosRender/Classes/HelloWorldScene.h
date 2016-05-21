#ifndef __HELLOWORLD_SCENE_H__
#define __HELLOWORLD_SCENE_H__

#define COCOS2D_DEBUG 1

#include "cocos2d.h"


#include "MySprite.h"
#define IMG_SIZE 9
#define SPRITE_INCREASE_STEP 100
class HelloWorld : public cocos2d::Layer
{
private:
	Image* pImageArr[IMG_SIZE];
	Texture2D * pTextureArr[IMG_SIZE];
	MySprite **pSpriteArray;

	int spriteSize;

	double spWidth;
	double spHeight;

	Size visibleSize;
	int screenWidth;
	int screenHeight;
	int leftBound;
	int rightBound;
	int topBound;
	int bottomBound;
	Label *labelFPS;
	Label *label;
	float fontSize;
public:
    static cocos2d::Scene* createScene();

    virtual bool init();
    
    // a selector callback
    void menuCloseCallback(cocos2d::Ref* pSender);

	void update(float) override;

	void explode();
    
    // implement the "static create()" method manually
    CREATE_FUNC(HelloWorld);
};

#endif // __HELLOWORLD_SCENE_H__
