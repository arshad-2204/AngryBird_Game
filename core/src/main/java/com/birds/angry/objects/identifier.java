package com.birds.angry.objects;

abstract class identifier {
    int identifier;
    int width;
    int height;
    identifier(int identifier, int width, int height) {
        this.identifier = identifier;
        this.width = width;
        this.height = height;
    }

    identifier(int identifier) {
        this.identifier = identifier;
        this.width = 0;
        this.height = 0;
    }

    public int getIdentifier() {
        return identifier;
    }
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
