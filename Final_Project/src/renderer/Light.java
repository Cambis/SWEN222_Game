package renderer;

import renderer.math.Vec3;

public class Light {
	private Vec3 position;
	private Vec3 direction;

	public Light(Vec3 position, Vec3 direction) {
		super();
		this.position = position.add(new Vec3(0, 0.2f, 0));
		this.direction = new Vec3(Math.sin(direction.getY()), -0.5f, Math.cos(direction.getY()));
	}
	public Vec3 getPosition() {
		return position;
	}
	public void setPosition(Vec3 position) {
		this.position = position;
	}
	public Vec3 getDirection() {
		return direction;
	}
	public void setDirection(Vec3 direction) {
		this.direction = direction;
	}


}
