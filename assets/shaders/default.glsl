#type vertex
#version 330 core

layout (location = 0) in vec3 aPos;
layout (location = 1) in vec4 aColor;
layout (location = 2) in vec2 aTexCoords;
layout (location = 3) in float aTexID;

uniform mat4 uProjection;
uniform mat4 uView;

out vec4 fColor;
out vec2 fTexCoords;
out float fTexID;

void main() {
    fColor = aColor;                     // Pass color to fragment shader
    fTexCoords = aTexCoords;             // Pass texture coordinates to fragment shader
    fTexID = aTexID;                     // Pass texture ID to fragment shader
    gl_Position = uProjection * uView * vec4(aPos, 1.0);  // Apply projection and view matrices
}

#type fragment
#version 330 core

in vec4 fColor;             // Interpolated color from vertex shader
in vec2 fTexCoords;        // Interpolated texture coordinates
in float fTexID;           // Interpolated texture ID

uniform sampler2D uTextures[8];  // Array of textures

out vec4 color;             // Final color output

void main() {
    int id = int(fTexID);      // Convert texture ID to an integer
    if (id >= 0 && id < 8) {   // Ensure the texture ID is valid (within the range of available textures)
        color = fColor * texture(uTextures[id], fTexCoords); // Sample the texture and apply color
    } else {
        // Magenta for invalid texture ID (used as error checking)
        color = vec4(1.0, 0.0, 1.0, 1.0);
    }
}
