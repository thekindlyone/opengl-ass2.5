package utils;

/**


 * A generic scene graph tree node.
 *
 * @author Jacqueline Whalley
 */

import java.util.LinkedList;
import java.util.List;

import com.jogamp.opengl.GL2;


public abstract class TreeNode {
  // list of children nodes
  private List<TreeNode> children = new LinkedList<TreeNode>();
  // Adds a child to the tree node
  public void addChild(TreeNode newChild)
  {
    children.add(newChild);
  }

  // drawing code for this branch of the tree
  public void draw(GL2 gl){
	  

   gl.glPushMatrix();

      // make the transformation for this branch of the tree
      transformNode(gl);

      // draw the current node
      drawNode(gl);

      // iterate through all the children
      for ( TreeNode child : children)
      {
        // go depth first into the tree
        child.draw(gl);
      }

    gl.glPopMatrix();
  }

  // Implement this method for transforming the node relative to its parent
  public abstract void transformNode(GL2 gl);

  // Implement this method to do the actual drawing of the node
  public abstract void drawNode(GL2 gl);

}
