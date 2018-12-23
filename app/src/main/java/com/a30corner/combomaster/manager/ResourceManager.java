package com.a30corner.combomaster.manager;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.audio.sound.SoundManager;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.font.StrokeFont;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.source.FileBitmapTextureAtlasSource;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;
import org.andengine.util.texturepack.TexturePack;
import org.andengine.util.texturepack.TexturePackLoader;
import org.andengine.util.texturepack.TexturePackTextureRegionLibrary;
import org.andengine.util.texturepack.exception.TexturePackParseException;

import android.graphics.Typeface;
import android.util.Pair;
import android.util.SparseArray;

import com.a30corner.combomaster.activity.GameActivity;
import com.a30corner.combomaster.pad.PadBoardAI.IMAGE_TYPE;
import com.a30corner.combomaster.texturepacker.GameUIAssets;
import com.a30corner.combomaster.texturepacker.SimulateAssets;
import com.a30corner.combomaster.texturepacker.TextureOrbs;
import com.a30corner.combomaster.texturepacker.TextureOrbsCw;
import com.a30corner.combomaster.texturepacker.TextureOrbsTos;
import com.a30corner.combomaster.texturepacker.TexturePackerManager;
import com.a30corner.combomaster.utils.LogUtil;
import com.a30corner.combomaster.utils.SharedPreferenceUtil;

public class ResourceManager {
	private static final ResourceManager INSTANCE = new ResourceManager();

	// common objects
	private GameActivity activity;
	private Engine engine;
	private Camera camera;
	private VertexBufferObjectManager vbom;

	private BitmapTextureAtlas fontTexture;
	private BitmapTextureAtlas fontTexture16;
	private BitmapTextureAtlas fontTextureStroke;
	private Font font = null;
	private Font fontSmall = null;
	private Font fontStroke = null;

	private Music mMusic;
	private Sound mSwap = null;
	private List<Sound> mComboSound = null;

	private HashMap<Object, Pair<ITextureRegion, BitmapTextureAtlas>> mTextures = new HashMap<Object, Pair<ITextureRegion, BitmapTextureAtlas>>();
	private TexturePackerManager mTexturePacker;
	private List<TexturePack> mPackLoader = new ArrayList<TexturePack>();

	private ResourceManager() {
	}

	public static ResourceManager getInstance() {
		return INSTANCE;
	}

	public Engine getEngine() {
		return engine;
	}

	public GameActivity getActivity() {
		return activity;
	}

	public Camera getCamera() {
		return camera;
	}

	public VertexBufferObjectManager getVBOM() {
		return vbom;
	}

	public void initialize(GameActivity activity) {
		this.activity = activity;
		this.engine = activity.getEngine();
		this.camera = engine.getCamera();
		this.vbom = engine.getVertexBufferObjectManager();

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		mTexturePacker = new TexturePackerManager();
		mTexturePacker.init();
	}

	public void loadFont() {
		TextureManager tm = activity.getTextureManager();
		FontManager fm = activity.getFontManager();

		// must use 512...
		fontTexture = new BitmapTextureAtlas(tm, 512, 512,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		font = new Font(fm, fontTexture, Typeface.create(Typeface.DEFAULT,
				Typeface.BOLD), 36, true, Color.WHITE);
		fontTexture16 = new BitmapTextureAtlas(tm, 512, 512,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		fontSmall = new Font(fm, fontTexture16, Typeface.create(
				Typeface.DEFAULT_BOLD, Typeface.BOLD), 20, true, Color.WHITE);
        fontTextureStroke = new BitmapTextureAtlas(tm, 512, 512,
                TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        fontStroke = new StrokeFont(fm, fontTextureStroke, Typeface.create(
                Typeface.SANS_SERIF, Typeface.BOLD), 21, true, Color.WHITE, 1, Color.BLACK);
		fontTexture.load();
		fontTexture16.load();
		fontTextureStroke.load();

		tm.loadTexture(this.fontTexture);
		tm.loadTexture(this.fontTexture16); 
		tm.loadTexture(this.fontTextureStroke);
		fm.loadFonts(font, fontSmall, fontStroke);
	}

	public void unloadFont() {
		fontTexture.unload();
		fontTexture16.unload();
		fontTextureStroke.unload();
		
		TextureManager tm = activity.getTextureManager();
		tm.unloadTexture(fontTexture);
		tm.unloadTexture(fontTexture16);
		tm.unloadTexture(fontTextureStroke);
		activity.getFontManager().unloadFonts(font, fontSmall, fontStroke);
	}

	public void loadMusic() {
        SharedPreferenceUtil instance = SharedPreferenceUtil
                .getInstance(activity);
//        if (instance.getSoundEnable() == false) {
//            return;
//        }
        String bgm = instance.getBgm();
        File file = new File(bgm);
        if (file.exists()) {
            try {
                mMusic = MusicFactory.createMusicFromFile(getEngine()
                        .getMusicManager(), file);
                mMusic.setLooping(true);
//                mMusic.play();
            } catch (IllegalStateException e) {
                LogUtil.e(e, e.toString());
            } catch (IOException e) {
                LogUtil.e(e, e.toString());
            }
        }
        SoundManager soundMgr = getEngine().getSoundManager();
        String soundPath = instance.getSound();
        
        File sound = new File(soundPath);
        if ( sound.exists() ) {
        	mSwap = SoundFactory.createSoundFromFile(soundMgr, sound);
        	mSwap.setLooping(false);
        	
        	mComboSound = new ArrayList<Sound>();
        	File dir = sound.getParentFile();
        	File[] fileList = dir.listFiles(new FileFilter() {
				
				@Override
				public boolean accept(File pathname) {
					return pathname.getName().startsWith("combo");
				}
			});
        	if ( fileList != null ) {
        		SparseArray<Sound> maps = new SparseArray<Sound>();
        		for(File f : fileList) {
        			String path = f.getName();
        			int no = -1;
        			try {
        				String number = path.replace("combo", "").split("\\.")[0];
        				no = Integer.parseInt(number);
            		} catch(Exception e) {
            		}
        			if ( no != -1 && f.exists() ) {
        				maps.put(no, SoundFactory.createSoundFromFile(soundMgr, f));
        			}
        		}
        		for(int i=1; i<20; ++i) {
        			Sound snd = maps.get(i, null);
        			if ( snd != null ) {
        				mComboSound.add(snd);
        			}
        		}
        	}
        }
        

	}
	
	public void playSwapSound() {
		if ( mSwap != null ) {
			mSwap.play();
		}
	}
	
	public void playComboSound(int c) {
		int index = c-1;
		if ( mComboSound != null ) {
			int size = mComboSound.size();
			if ( index >= 0 && index < size ) {
				mComboSound.get(index).play();
			} else if ( size > 0 ){
				int max = size-1;
				mComboSound.get(max).play();
			}
		}
	}
	
	public void playMusic() {
	    if ( mMusic != null && !mMusic.isPlaying() ) {
	        mMusic.play();
	    }
	}
	
	public void pauseMusic() {
	    if ( mMusic != null && mMusic.isPlaying() ) {
	        mMusic.pause();
	    }
	}

	public void unloadMusic() {
        if (mMusic != null) {
            mMusic.stop();
            mMusic.release();
        }

        if ( mSwap != null ) {
        	mSwap.stop();
        	mSwap.release();
        	mSwap = null;
            for(Sound s : mComboSound) {
            	s.stop();
            	s.release();
            }
            mComboSound.clear();
        }
        

	}

	public void loadMenuScene() {
		TextureManager tm = activity.getTextureManager();

		addTexture(tm, 480, 225, TextureOptions.BILINEAR, "title.png");
	}

	public void unloadMenuScene() {
		disposeTexture("title.png");
	}

	public void unloadTextureFile(int name) {
		disposeTexture(String.valueOf(name));
	}

	public ITextureRegion loadTextureFile(String name) {
		File imageFile = new File(activity.getFilesDir(), name);
		if (imageFile.exists()) {
			FileBitmapTextureAtlasSource fileTextureSource = FileBitmapTextureAtlasSource
					.create(imageFile);
			BitmapTextureAtlas texture = new BitmapTextureAtlas(
					activity.getTextureManager(), 100, 100,
					TextureOptions.BILINEAR_PREMULTIPLYALPHA);
			TextureRegion textureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromSource(texture, fileTextureSource, 0, 0);
			texture.load();
			mTextures.put(name, new Pair<ITextureRegion, BitmapTextureAtlas>(
					textureRegion, texture));
			return textureRegion;
		}
		return null;
	}

	private void addTexture(Class<?> clz, int id, ITextureRegion region) {
		mTextures.put(mTexturePacker.getID(clz, id), new Pair<ITextureRegion, BitmapTextureAtlas>(region, null));
	}
	
	private void addTexture(TextureManager tm, int w, int h,
			TextureOptions opt, String name) {
		BitmapTextureAtlas bitmap = new BitmapTextureAtlas(tm, w, h, opt);
		ITextureRegion texture = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(bitmap, activity, name, 0, 0);
		bitmap.load();
		mTextures.put(name, new Pair<ITextureRegion, BitmapTextureAtlas>(
				texture, bitmap));
	}

	public void loadLoadingScene() {
		TextureManager tm = activity.getTextureManager();
		addTexture(tm, 377, 92, TextureOptions.BILINEAR, "loading.png");
	}

	public void unloadLoadingScene() {
		disposeTexture("loading.png");
	}

	public void disposeTextures() {
		for (Object key : mTextures.keySet()) {
			disposeTexture(key);
		}
	}

	private void disposeTexture(Object key) {
		if (mTextures.containsKey(key)) {
			Pair<ITextureRegion, BitmapTextureAtlas> pair = mTextures.get(key);
			if(pair.second != null) {
				pair.second.unload();
			}
			mTextures.remove(key);
		}
	}

	public void loadGameScene() {
		loadGameScene(false);
	}
	
	public void loadGameScene(boolean forceShine) {
		TextureManager tm = activity.getTextureManager();

		SharedPreferenceUtil utils = SharedPreferenceUtil.getInstance(activity);
		IMAGE_TYPE type = (utils.getType() == 2) ? IMAGE_TYPE.TYPE_TOS
				: IMAGE_TYPE.TYPE_PAD;

		if (utils.getBoolean(SharedPreferenceUtil.PREF_COLOR_WEAKNESS)
				&& type == IMAGE_TYPE.TYPE_PAD) {
			type = IMAGE_TYPE.TYPE_PAD_CW;
		}
		
		TexturePackLoader loader = new TexturePackLoader(activity.getAssets(), tm);
		try {
			String xml = null;
			Class<?> clz = null;
			switch(type) {
			case TYPE_PAD_CW:
				xml = "xml/TextureOrbsCw.xml";
				clz = TextureOrbsCw.class;
				break;
			case TYPE_TOS:
				xml = "xml/TextureOrbsTos.xml";
				clz = TextureOrbsTos.class;
				break;
			case TYPE_PAD:
			default:
				xml = "xml/TextureOrbs.xml";
				clz = TextureOrbs.class;
				break;					
			}
			loadTexturePacker(loader, clz, xml);
		} catch (TexturePackParseException e) {
			LogUtil.e(e, e.toString());
		}
		
		try {
			loadTexturePacker(loader, GameUIAssets.class, "xml/GameUIAssets.xml");
			loadTexturePacker(loader, SimulateAssets.class, "xml/SimulateAssets.xml");
		} catch (TexturePackParseException e) {
			LogUtil.e(e, e.toString());
		}
		
		// for simulate
		addTexture(tm, 40, 40, TextureOptions.BILINEAR, "cross_shield.png");
		
		addTexture(tm, 256, 256, TextureOptions.BILINEAR, "background.png");
		addTexture(tm, 50, 50, TextureOptions.BILINEAR, "dark.png");
		addTexture(tm, 1, 10, TextureOptions.BILINEAR, "hp.png");

		addTexture(tm, 1, 12, TextureOptions.BILINEAR, "ehp.png");
		addTexture(tm, 1, 4, TextureOptions.BILINEAR, "ehp_0.png");
        addTexture(tm, 1, 4, TextureOptions.BILINEAR, "ehp_1.png");
        addTexture(tm, 1, 4, TextureOptions.BILINEAR, "ehp_3.png");
        addTexture(tm, 1, 4, TextureOptions.BILINEAR, "ehp_4.png");
        addTexture(tm, 1, 4, TextureOptions.BILINEAR, "ehp_5.png");
        addTexture(tm, 32, 32, TextureOptions.BILINEAR, "texture.png");
        addTexture(tm, 64, 64, TextureOptions.BILINEAR, "kage.png");
		addTexture(tm, 88, 88, TextureOptions.BILINEAR, "bomb.png");
		addTexture(tm, 100, 100, TextureOptions.BILINEAR, "cloud.png");
		addTexture(tm, 32, 32, TextureOptions.BILINEAR, "cloud_s.png");

		final String[] img = {
				"xdark.png", "xfire.png", "xheart.png", "xlight.png", "xwater.png", "xwood.png"
		};
		for(String i : img) {
			addTexture(tm, 38, 38, TextureOptions.BILINEAR, i);
		}
//		addTexture(tm, 88, 88, TextureOptions.BILINEAR, "change_bg.png");
//		addTexture(tm, 28, 28, TextureOptions.BILINEAR, "change.png");

        boolean shine = SharedPreferenceUtil.getInstance(activity).getBoolean(SharedPreferenceUtil.PREF_SHINE_EFFECT, false);
        if (forceShine || shine) {
	        addTexture(tm, 88, 88, TextureOptions.BILINEAR, "flash01.png");
	        addTexture(tm, 88, 88, TextureOptions.BILINEAR, "flash02.png");
	        addTexture(tm, 88, 88, TextureOptions.BILINEAR, "flash03.png");
	        addTexture(tm, 88, 88, TextureOptions.BILINEAR, "rflash01.png");
	        addTexture(tm, 88, 88, TextureOptions.BILINEAR, "rflash02.png");
	        addTexture(tm, 88, 88, TextureOptions.BILINEAR, "rflash03.png");
        }
	}

	private void loadTexturePacker(TexturePackLoader loader, Class<?> clz, String xml) throws TexturePackParseException {
		TexturePack pack = loader.loadFromAsset(xml, "gfx/");
		pack.loadTexture();
		TexturePackTextureRegionLibrary library = pack.getTexturePackTextureRegionLibrary();
		
		Field[] field = clz.getDeclaredFields();
		for(Field f : field) {
			try {
				int id = f.getInt(null);
				addTexture(clz, id, library.get(id));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		mPackLoader.add(pack);
	}
	
	public void unloadGameScene() {
		for(TexturePack pack : mPackLoader) {
			pack.unloadTexture();
		}
//		mTexturePacker.release();
		mPackLoader.clear();
		disposeTexture("cross_shield.png");

		disposeTexture("bomb.png");

		disposeTexture("dark.png");
		disposeTexture("hp.png");

//		disposeTexture("change.png");
//		disposeTexture("change_bg.png");

        disposeTexture("ehp.png");
        disposeTexture("ehp_0.png");
        disposeTexture("ehp_1.png");
        disposeTexture("ehp_3.png");
        disposeTexture("ehp_4.png");
        disposeTexture("ehp_5.png");
        disposeTexture("texture.png");
        disposeTexture("kage.png");
        disposeTexture("cloud.png");
        disposeTexture("cloud_s.png");

		final String[] img = {
				"xdark.png", "xfire.png", "xheart.png", "xlight.png", "xwater.png", "xwood.png"
		};
		for(String i : img) {
			disposeTexture(i);
		}

        boolean shine = SharedPreferenceUtil.getInstance(activity).getBoolean(SharedPreferenceUtil.PREF_SHINE_EFFECT, false);
        if (shine) {
	        disposeTexture("flash01.png");
	        disposeTexture("flash02.png");
	        disposeTexture("flash03.png");
	        disposeTexture("rflash01.png");
	        disposeTexture("rflash02.png");
	        disposeTexture("rflash03.png");
        }
//		unloadOrb();
//		unloadGameSceneGeneric();
	}

	public ITextureRegion getTextureRegion(Class<?> clz, int id) {
		if ( id == 9999 ) {
			return mTextures.get("bomb.png").first;
		} else if (id == 1001) {
			return mTextures.get("cloud_s.png").first;
		} else if (id >= 10001 && id <= 10006) {
			int index = id - 10001;
			final String[] img = {
					"xdark.png", "xfire.png", "xheart.png", "xlight.png", "xwater.png", "xwood.png"
			};
			return mTextures.get(img[index]).first;
		}
		int key = mTexturePacker.getID(clz, id);
		return getTextureRegion(key);
	}
	
	public ITextureRegion getTextureRegion(Object key) {
		if (mTextures.containsKey(key)) {
			return mTextures.get(key).first;
		}
		return null;
	}

	public IFont getFont() {
		return font;
	}

	public IFont getFontSmall() {
		return fontSmall;
	}
	
	public IFont getFontStroke() {
	    return fontStroke;
	}
}
