#type vertex
#version 330 core

layout (location = 0) in vec3 aPos;
layout (location = 1) in vec4 aColor;
layout (location = 2) in vec2 aTexCoords;
layout (location = 3) in float aTexID;
layout (location = 4) in float aEntityId;

uniform mat4 uProjection;
uniform mat4 uView;

out vec4 fColor;
out vec2 fTexCoords;
out float fTexID;
out float fEntityId;

void main() {
    fColor = aColor;                     // Pass color to fragment shader
    fTexCoords = aTexCoords;             // Pass texture coordinates to fragment shader
    fTexID = aTexID;                     // Pass texture ID to fragment shader
    fEntityId =  aEntityId;
    gl_Position = uProjection * uView * vec4(aPos, 1.0);  // Apply projection and view matrices
}

#type fragment
#version 330 core

in vec4 fColor;             // Interpolated color from vertex shader
in vec2 fTexCoords;        // Interpolated texture coordinates
in float fTexID;           // Interpolated texture ID
in float fEntityId;          // Interpolated entity ID

uniform sampler2D uTextures[8];  // Array of textures

out vec3 color;             // Final color output

void main() {

    vec4 texColor = vec4(1, 1, 1, 1);
    int id = int(fTexID);      // Convert texture ID to an integer
    if (id > 0 ) {

        texColor = fColor * texture(uTextures[id], fTexCoords);
    }
    if(texColor.a < 0.5) {
        discard; // Discard this fragment if alpha value is less than 0.5 (transparent)
    }

    color = vec3(fEntityId, fEntityId, fEntityId);
}
