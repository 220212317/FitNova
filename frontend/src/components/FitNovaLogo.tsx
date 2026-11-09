import React from "react";

interface FitNovaLogoProps {
    size?: number;
    className?: string;
}

export function FitNovaLogo({ size = 32, className = "" }: FitNovaLogoProps) {
    return (
        <svg
            width={size}
            height={size}
            viewBox="0 0 100 100"
            fill="none"
            xmlns="http://www.w3.org/2000/svg"
            className={className}
            aria-label="FitNova Logo"
        >
            <g fill="#5de83d">
                {/* Top shape: The cap and right arm */}
                <path d="
          M 26 18
          C 31 11 38 7 46 7
          H 86
          L 76 22
          C 73 26 68 28 62 28
          H 46
          C 40 28 36 32 33 38
          L 29 45
          L 15 45
          L 20 28
          C 22 23 24 20 26 18 Z
        "/>

                {/* Bottom shape: The vertical stem and lower arm */}
                <path d="
          M 28 49
          H 69
          L 59 64
          C 56 68 51 70 45 70
          H 37
          L 30 86
          C 28 91 23 95 16 95
          H 12
          L 22 61
          C 23 53 25 49 28 49 Z
        "/>
            </g>
        </svg>
    );
}