#include "HelloWorldScene.h"
#include "SimpleAudioEngine.h"

USING_NS_CC;

# define M_PI           3.14159265358979323846

Scene* HelloWorld::createScene()
{
    // 'scene' is an autorelease object
    auto scene = Scene::create();
    
    // 'layer' is an autorelease object
    auto layer = HelloWorld::create();

    // add layer as a child to scene
    scene->addChild(layer);

    // return the scene
    return scene;
}

// on "init" you need to initialize your instance
bool HelloWorld::init()
{
    //////////////////////////////
    // 1. super init first
    if ( !Layer::init() )
    {
        return false;
    }
    
    visibleSize = Director::getInstance()->getVisibleSize();
    Vec2 origin = Director::getInstance()->getVisibleOrigin();

    /////////////////////////////
    // 2. add a menu item with "X" image, which is clicked to quit the program
    //    you may modify it.

    // add a "close" icon to exit the progress. it's an autorelease object
    auto closeItem = MenuItemImage::create(
                                           "CloseNormal.png",
                                           "CloseSelected.png",
                                           CC_CALLBACK_1(HelloWorld::menuCloseCallback, this));
    
    closeItem->setPosition(Vec2(origin.x + visibleSize.width - closeItem->getContentSize().width/2 ,
                                origin.y + closeItem->getContentSize().height/2));

    // create menu, it's an autorelease object
    auto menu = Menu::create(closeItem, NULL);
    menu->setPosition(Vec2::ZERO);
    this->addChild(menu, 1);

    /////////////////////////////
    // 3. add your codes below...
	auto touchListener = EventListenerTouchOneByOne::create();
	

	touchListener->onTouchBegan = [](Touch* touch, Event* event) {
		//CCLOG("onTouchBegin");
		return true;
	};

	// trigger when moving touch
	touchListener->onTouchMoved = [](Touch* touch, Event* event) {
		//CCLOG("onTouchMove");
	};

	// trigger when you let up
	touchListener->onTouchEnded = [=](Touch* touch, Event* event) {
		HelloWorld *ph = (HelloWorld *)event->getCurrentTarget();
		ph->explode();
	};

	_eventDispatcher->addEventListenerWithSceneGraphPriority(touchListener, this);

    // add a label shows "Hello World"
    // create and initialize a label
	std::string str= "Cocos2d-x,  Sprite Amount:";
	char s[8];
	this->spriteSize = 0;
	this->pSpriteArray = NULL;
	sprintf(s, "%d", spriteSize);
	str += s;
	str += ", FPS:";

	fontSize = 50;
    label = Label::createWithTTF(str, "fonts/arial.ttf", fontSize);

	labelFPS = Label::createWithTTF("0", "fonts/arial.ttf", fontSize);

	
    
    // position the label on the center of the screen
    label->setPosition(Vec2( label->getContentSize().width/2,
                            origin.y + visibleSize.height - label->getContentSize().height));

	labelFPS->setPosition(Vec2(label->getContentSize().width + fontSize,
		origin.y + visibleSize.height - labelFPS->getContentSize().height));
    // add the label as a child to this layer
    this->addChild(label, 1);
	this->addChild(labelFPS, 1);

    // add "HelloWorld" splash screen"
    auto sprite = Sprite::create("HelloWorld.png");

    // position the sprite on the center of the screen
    sprite->setPosition(Vec2(visibleSize.width/2 + origin.x, visibleSize.height/2 + origin.y));

   

	//load images
	spWidth = 32;
	spHeight = 32;
	screenWidth = visibleSize.width;
	screenHeight = visibleSize.height;
	leftBound = spWidth / 2;
	rightBound = screenWidth - spWidth / 2;
	topBound = spHeight / 2;
	bottomBound = screenHeight - spHeight / 2 - 100;

	CCLOG("screenWidth=%d, screenHeight=%d", screenWidth, screenHeight);

	char filename[] = "ball0.png";
	int pos = 4;
	for (int i = 0; i < IMG_SIZE; ++i) {
		this->pImageArr[i] = new Image();
		filename[4] = '1' + i;
		pImageArr[i]->initWithImageFile(filename);
		pTextureArr[i] = new Texture2D();
		pTextureArr[i]->initWithImage(pImageArr[i]);
	}

	

	

	// add the sprite as a child to this layer
	this->addChild(sprite, 1);
	
	sprite->setOpacity(185);

	this->scheduleUpdate();
    return true;
}

void HelloWorld::explode() {
	
	for (int i = 0; i < this->spriteSize; ++i) {
		this->removeChild(pSpriteArray[i]->sp);
		delete pSpriteArray[i];
	}
	delete[] pSpriteArray;

	this->spriteSize += SPRITE_INCREASE_STEP;
	pSpriteArray = new MySprite*[this->spriteSize];
	int seg = this->spriteSize / IMG_SIZE;
	int count = 0;
	int id = 0;
	double degree = 0;
	double speed = 0;
	for (int i = 0; i < this->spriteSize; ++i) {
		++count;
		if (count > seg) {
			count = 0;
			++id;
		}
		degree = rand() / double(RAND_MAX) * M_PI *2.0;
		speed = 1.0 + rand() / double(RAND_MAX) * 10.0;

		//*rand() / double(RAND_MAX)
		this->pSpriteArray[i] = new MySprite(this->pTextureArr[id % 9], visibleSize.width / 2, visibleSize.height / 2);
		pSpriteArray[i]->setSize((float)spWidth, (float)spHeight);
		this->addChild(pSpriteArray[i]->sp, 0);
		pSpriteArray[i]->speedX = speed*cos(degree);
		pSpriteArray[i]->speedY = speed*sin(degree);
	}

	// position the label on the center of the screen
	Vec2 origin = Director::getInstance()->getVisibleOrigin();

	std::string str = "Cocos2d-x,  Sprite Amount:";
	char s[8];
	sprintf(s, "%d", spriteSize);
	str += s;
	str += ", FPS:";

	label->setString(str);

	label->setPosition(Vec2(label->getContentSize().width / 2,
		origin.y + visibleSize.height - label->getContentSize().height));

	labelFPS->setPosition(Vec2(label->getContentSize().width + fontSize,
		origin.y + visibleSize.height - labelFPS->getContentSize().height));
}

static double getSign(double v) {
	return v >= 0.0 ? 1.0 : -1.0;
}
void HelloWorld::update(float delta) {
	auto director = Director::getInstance();
	int fps = (int)director->getFrameRate();
	char s[4];
	sprintf(s, "%d", fps);
	this->labelFPS->setString(s);

	MySprite *p;
	double speedX, speedY;
	for (int i = 0; i < this->spriteSize; ++i) {
		p = pSpriteArray[i];
		


		speedX = p->speedX;
		speedY = p->speedY;
		double sign = 0;
		if (p->posX < leftBound || p->posX > rightBound) {
			speedX = - speedX;
			sign = getSign(speedX);
			if (abs(speedX) <= 1.0) {
				speedX = sign*(abs(speedX) + 5.0 * rand() / double(RAND_MAX));
			}
		}

		if (p->posY < topBound || p->posY  > bottomBound) {
			speedY = -speedY;
			sign = getSign(speedY);
			if (abs(speedY) <= 1.0) {
				speedY = sign*(abs(speedY) + 5.0 * rand() / double(RAND_MAX));
			}
		}

		
		

		p->speedX = speedX;
		p->speedY = speedY;

		p->posX += p->speedX;
		p->posY += p->speedY;
		p->sp->setPosition(p->posX, p->posY);
	}
}

void HelloWorld::menuCloseCallback(Ref* pSender)
{
    Director::getInstance()->end();

#if (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)
    exit(0);
#endif
}
