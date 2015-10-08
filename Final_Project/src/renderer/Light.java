package renderer;

import renderer.math.Vec3;

public class Light {
	private Vec3 position;
	private Vec3 direction;
	private R_Player.Team side;

	public Light(Vec3 position, Vec3 direction, R_Player.Team side) {
		super();
		float xx = (float)Math.sin(direction.getY());
		float zz = (float)Math.cos(direction.getY());
		this.position = position.add(new Vec3(xx*0.1f, 0.2f, zz*0.1f));
		this.direction = new Vec3(xx, -0.5f, zz);
		this.side = side;
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
	public R_Player.Team getSide() {
		return side;
	}
	public void setSide(R_Player.Team side) {
		this.side = side;
	}
}
