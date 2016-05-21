

#include "MySprite.h"

void MySprite::setSize(float w, float h) {
	sp->setScaleX(w/sp->getContentSize().width);
	sp->setScaleY(h / sp->getContentSize().height);
}

MySprite::MySprite(Texture2D *p, double w, double h) {
	this->sp = Sprite::createWithTexture(p);
	sp->setPosition(w, h);
	this->posX = w;
	this->posY = h;
	this->speedX = 0;
	this->speedY = 0;
}

MySprite::~MySprite() {

}