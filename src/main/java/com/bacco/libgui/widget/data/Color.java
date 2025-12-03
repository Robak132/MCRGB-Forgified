package com.bacco.libgui.widget.data;

public interface Color {
	Color WHITE = rgb(0xFF_FFFFFF);
	Color BLACK = rgb(0xFF_000000);
	Color RED   = rgb(0xFF_FF0000);
	Color GREEN = rgb(0xFF_00FF00);
	Color BLUE  = rgb(0xFF_0000FF);
	
	Color WHITE_DYE      = rgb(0xFF_F9FFFE);
	Color ORANGE_DYE     = rgb(0xFF_F9801D);
	Color MAGENTA_DYE    = rgb(0xFF_C74EBD);
	Color LIGHT_BLUE_DYE = rgb(0xFF_3AB3DA);
	Color YELLOW_DYE     = rgb(0xFF_FED83D);
	Color LIME_DYE       = rgb(0xFF_80C71F);
	Color PINK_DYE       = rgb(0xFF_F38BAA);
	Color GRAY_DYE       = rgb(0xFF_474F52);
	Color LIGHT_GRAY_DYE = rgb(0xFF_9D9D97);
	Color CYAN_DYE       = rgb(0xFF_169C9C);
	Color PURPLE_DYE     = rgb(0xFF_8932B8);
	Color BLUE_DYE       = rgb(0xFF_3C44AA);
	Color BROWN_DYE      = rgb(0xFF_835432);
	Color GREEN_DYE      = rgb(0xFF_5E7C16);
	Color RED_DYE        = rgb(0xFF_B02E26);
	Color BLACK_DYE      = rgb(0xFF_1D1D21);
	
	Color[] DYE_COLORS = {
			WHITE_DYE,      ORANGE_DYE, MAGENTA_DYE, LIGHT_BLUE_DYE,
			YELLOW_DYE,     LIME_DYE,   PINK_DYE,    GRAY_DYE,
			LIGHT_GRAY_DYE, CYAN_DYE,   PURPLE_DYE,  BLUE_DYE,
			BROWN_DYE,      GREEN_DYE,  RED_DYE,     BLACK_DYE
	};

	/**
	 * Gets an ARGB integer representing this color in the sRGB colorspace.
	 */
    int toRgb();
	
	
	static Color rgb(int value) {
		return new RGB(value);
	}

    record RGB(int value) implements Color {

        public RGB(int a, int r, int g, int b) {
            this(((a & 0xFF) << 24) |
              ((r & 0xFF) << 16) |
              ((g & 0xFF) << 8) |
              (b & 0xFF));
        }

        /**
         * Constructs an RGB object with 100% alpha value (no transparency)
         *
         * @since 2.0.0
         */
        public RGB(int r, int g, int b) {
            this((0xFF << 24) |
              ((r & 0xFF) << 16) |
              ((g & 0xFF) << 8) |
              (b & 0xFF));
        }

        @Override
        public int toRgb() {
            return value;
        }

        public int getA() {
            return (value >> 24) & 0xFF;
        }

        public int getR() {
            return (value >> 16) & 0xFF;
        }

        public int getG() {
            return (value >> 8) & 0xFF;
        }

        public int getB() {
            return value & 0xFF;
        }

        /**
         * Gets the chroma value, which is related to the length of the vector in projected (hexagonal) space.
         */
        public int getChroma() {
            int r = getR();
            int g = getG();
            int b = getB();

            int max = Math.max(Math.max(r, g), b);
            int min = Math.min(Math.min(r, g), b);
            return max - min;
        }

        /**
         * Gets the HSV/HSL Hue, which is the angle around the color hexagon (or circle)
         */
        public int getHue() {
            float r = getR() / 255f;
            float g = getG() / 255f;
            float b = getB() / 255f;

            float max = Math.max(Math.max(r, g), b);
            float min = Math.min(Math.min(r, g), b);
            float chroma = max - min;

            if (chroma == 0) return 0;

            if (max >= r) return
              (int) ((((g - b) / chroma) % 6) * 60);
            if (max >= g) return
              (int) ((((b - r) / chroma) + 2) * 60);
            if (max >= b) return
              (int) ((((r - g) / chroma) + 4) * 60);

            //Mathematically, we shouldn't ever reach here
            return 0;
        }

        /**
         * Gets the HSL Lightness, or average light intensity, of this color
         */
        public int getLightness() {
            int r = getR();
            int g = getG();
            int b = getB();

            int max = Math.max(Math.max(r, g), b);
            int min = Math.min(Math.min(r, g), b);
            return (max + min) / 2;
        }

        /**
         * Gets the HSL Luma, or perceptual brightness, of this color
         */
        public int getLuma() {
            float r = getR() / 255f;
            float g = getG() / 255f;
            float b = getB() / 255f;

            return (int) (((0.2126f * r) + (0.7152f * g) + (0.0722f * b)) * 255);
        }

        /**
         * Gets the HSV Value, which is just the largest component in the color
         */
        @Override
        public int value() {
            int r = getR();
            int g = getG();
            int b = getB();

            return Math.max(Math.max(r, g), b);
        }

        /**
         * Gets the saturation for this color based on chrominance and HSV Value
         */
        public float getHSVSaturation() {
            float v = value(); //I don't rescale these to 0..1 because it's just the ratio between them
            if (v == 0) return 0;
            float c = getChroma();
            return c / v;
        }

        /**
         * Gets the saturation for this color based on chrominance and HSL <em>luma</em>.
         */
        public float getHSLSaturation() {
            float l = getLuma() / 255f; //rescaled here because there's more than just a ratio going on
            if (l == 0 || l == 1) return 0;
            float c = getChroma() / 255f;
            return c / (1 - Math.abs(2 * l - 1));
        }

        /**
         * Calculates an interpolated value along the fraction t between 0.0 and 1.0. When t = 1.0, endVal is returned.
         * Eg.: If this color is black, your endColor is white and t = 0.5 you get gray.
         *
         * @param endColor a Color to interpolate with
         * @param t        fraction between 0.0 and 1.0
         * @since 2.3.0
         */
        public RGB interpolate(RGB endColor, double t) {
            double a = (endColor.getA() - this.getA()) * t + this.getA();
            double r = (endColor.getR() - this.getR()) * t + this.getR();
            double g = (endColor.getG() - this.getG()) * t + this.getG();
            double b = (endColor.getB() - this.getB()) * t + this.getB();
            return new RGB((int) a, (int) r, (int) g, (int) b);
        }
    }

}
