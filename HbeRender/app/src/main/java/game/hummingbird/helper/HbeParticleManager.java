package game.hummingbird.helper;

public class HbeParticleManager
{
    public 	HbeParticleManager(){
        _nPS = 0;
        _tX = _tY = 0.0f;
    }

    public void				update(float dt){
        HbeParticleSystem psys = _psList;
        while(psys!=null)
        {
            //psys =
        }
    }
    public	void				render(){
        ;
    }

    public	HbeParticleSystem	spawnPS(HbeParticleSystemInfo si, float x, float y){
        return null;
    }
    public	boolean				isPSAlive(HbeParticleSystem ps) {
        return false;
    }
    public	void				transpose(float x, float y){
        ;
    }
    public	float				getTranspositionX() {return _tX;}
    public	float				getTranspositionY() {return _tY;}
    public	void				killPS(HbeParticleSystem ps){
        ;
    }
    public	void				killAll(){
        ;
    }

    private	int					_nPS;
    private	float				_tX;
    private	float				_tY;
    private	HbeParticleSystem	_psList;
}