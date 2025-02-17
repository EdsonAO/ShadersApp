package com.earratea.shadersapp.ux.complex

import org.intellij.lang.annotations.Language

@Language("AGSL")
val ComplexShader1 = """
    uniform float2 resolution;
    uniform float time;
    
    half4 main(in float2 fragCoord) {
        float4 o = float4(0);
        float2 p = float2(0);
        float2 c = p;
        float2 u = fragCoord.xy * 2.0 - resolution.xy;
        float a;
        for (float i=0; i<4e2; i++) {
            a = i/2e2-1.;
            p = cos(i*2.4+time+float2(0,11))*sqrt(1.-a*a);
            c = u/resolution.y+float2(p.x,a)/(p.y+2.);
            o += (cos(i+float4(0,2,4,0))+1.)/dot(c,c)*(1.-p.y)/3e4;
        }
        return o;
    }
"""