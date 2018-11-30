// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2018
// diego.catalano at live.com
//
//    This library is free software; you can redistribute it and/or
//    modify it under the terms of the GNU Lesser General Public
//    License as published by the Free Software Foundation; either
//    version 2.1 of the License, or (at your option) any later version.
//
//    This library is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
//    Lesser General Public License for more details.
//
//    You should have received a copy of the GNU Lesser General Public
//    License along with this library; if not, write to the Free Software
//    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
//

package Catalano.Imaging.Tools;

/**
 * References of color card (sRGB values).
 * @author Diego Catalano
 */
public class ColorCard {
    
    /**
     * X-Rite© ColorChecker Passport.
     */
    public static int[][] ColorCheckerPassport = new int[][] {
        {115,82,68},    //Dark Skin
        {194,150,130},  //Light Skin
        {98,112,157},   //Blue Sky
        {87,108,67},    //Foliage
        {133,128,177},  //Blue Flower
        {103,189,170},  //Bluish Green
        
        {214,126,44},   //Orange
        {80,91,166},    //Purple Red
        {193,90,99},    //Moderate Red
        {94,60,108},    //Purple
        {157,188,64},   //Yellow Green
        {224,163,46},   //Orange Yellow
        
        {56,61,150},    //Blue
        {70,148,73},    //Green
        {175,54,60},    //Red
        {231,199,31},   //Yellow
        {187,86,149},   //Magenta
        {8,133,161},    //Cyan
        
        {243,243,243},  //White
        {200,200,200},  //Neutral 8
        {160,160,160},  //Neutral 65
        {122,122,121},  //Neutral 5
        {85,85,85},     //Neutral 35
        {52,52,52},     //Black
        
    };
    
    /**
     * Datacolor SpyderCHECKR(tm) 24.
     */
    public static int[][] SpyderCheckr24 = new int[][] {
        {43,41,43},     //Card Black
        {80,80,78},     //80% Gray
        {122,118,116},  //60% Gray
        {161,157,14},   //40% Gray
        {202,198,195},  //20% Gray
        {249,242,238},  //Card White
        
        {25,55,135},    //Primary Blue
        {57,146,64},    //Primary Green
        {186,26,51},    //Primary Red
        {245,205,0},    //Primary Yellow
        {192,75,145},   //Primary Magenta
        {0,127,159},    //Primary Cyan
        
        {238,158,25},   //Sunflower
        {157,188,54},   //Apple Green
        {83,58,106},    //Violet
        {195,79,95},    //Pink
        {58,88,159},    //Blueprint
        {222,118,32},   //Primary Orange
        
        {112,76,60},    //Classic Dark Skin
        {197,145,125},  //Classic Light Skin
        {87,120,155},   //Steel Blue
        {82,106,60},    //Evergreen
        {126,125,174},  //Lavender
        {98,187,166},   //Aqua
        
    };

    private ColorCard() {}
    
}