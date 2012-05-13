package org.andengine.extension.input.touch.controller;

import org.andengine.extension.input.touch.exception.MultiTouchException;
import org.andengine.input.touch.controller.BaseTouchController;

import android.view.MotionEvent;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 20:23:33 - 13.07.2010
 */
public class MultiTouchController extends BaseTouchController {
        // ===========================================================
        // Constants
        // ===========================================================

        // ===========================================================
        // Fields
        // ===========================================================

        // ===========================================================
        // Constructors
        // ===========================================================
        
        public MultiTouchController() throws MultiTouchException {
                if(MultiTouch.isSupportedByAndroidVersion() == false) {
                        throw new MultiTouchException();
                }
        }

        // ===========================================================
        // Getter & Setter
        // ===========================================================

        // ===========================================================
        // Methods for/from SuperClass/Interfaces
        // ===========================================================

        @Override
        public void onHandleMotionEvent(final MotionEvent pMotionEvent) {
                final int action = pMotionEvent.getAction() & MotionEvent.ACTION_MASK;
                switch(action) {
                        case MotionEvent.ACTION_DOWN:
                        case MotionEvent.ACTION_POINTER_DOWN:
                                 this.onHandleTouchAction(MotionEvent.ACTION_DOWN, pMotionEvent);
                                 return;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_POINTER_UP:
                                 this.onHandleTouchAction(MotionEvent.ACTION_UP, pMotionEvent);
                                 return;
                        case MotionEvent.ACTION_CANCEL:
                        case MotionEvent.ACTION_OUTSIDE:
                                 this.onHandleTouchAction(action, pMotionEvent);
                                 return;
                        case MotionEvent.ACTION_MOVE:
                                 this.onHandleTouchMove(pMotionEvent);
                                 return;
                        default:
                                throw new IllegalArgumentException("Invalid Action detected: " + action);
                }
        }

        // ===========================================================
        // Methods
        // ===========================================================

        private boolean onHandleTouchMove(final MotionEvent pMotionEvent) {
                boolean handled = false;
                for(int i = pMotionEvent.getPointerCount() - 1; i >= 0; i--) {
                        final int pointerIndex = i;
                        final int pointerID = pMotionEvent.getPointerId(pointerIndex);
                        final boolean handledInner = true;
                        this.fireTouchEvent(pMotionEvent.getX(pointerIndex), pMotionEvent.getY(pointerIndex), MotionEvent.ACTION_MOVE, pointerID, pMotionEvent);
                        handled = handled || handledInner;
                }
                return handled;
        }
        
        private boolean onHandleTouchAction(final int pAction, final MotionEvent pMotionEvent) {
                final int pointerIndex = this.getPointerIndex(pMotionEvent);
                final int pointerID = pMotionEvent.getPointerId(pointerIndex);
                
                 this.fireTouchEvent(pMotionEvent.getX(pointerIndex), pMotionEvent.getY(pointerIndex), pAction, pointerID, pMotionEvent);
                 return true;
        }

        private int getPointerIndex(final MotionEvent pMotionEvent) {
                return (pMotionEvent.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
        }

        // ===========================================================
        // Inner and Anonymous Classes
        // ===========================================================
}
